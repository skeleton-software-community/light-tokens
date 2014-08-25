package free.lighttokens.core.components;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import free.lightokens.api.builder.EntityBuilder;
import free.lightokens.api.components.LTokenManager;
import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.exception.TokenAuthenticationException;
import free.lightokens.api.exception.TokenDecipherException;
import free.lightokens.api.exception.TokenGenerationException;
import free.lightokens.api.model.LTokenConfiguration;
import free.lightokens.api.model.LTokenInfo;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lightokens.api.model.entity.InvalidLToken;
import free.lighttokens.core.LoggerNames;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.core.util.TokenSerializer;
import free.lighttokens.core.util.cipher.TokenCipher;
import free.lighttokens.core.util.cipher.TokenDecipher;
import free.lighttokens.core.util.genertor.HashGenerator;
import free.lighttokens.exceptions.BadTokenVersionException;
import free.lighttokens.exceptions.MalformedTokenException;

public class LTokenManagerImpl implements LTokenManager{

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerNames.MANAGER_LOGGER);
	
	private TokenCipher cipher;
	private TokenDecipher decipher;
	private TokenSerializer serializer;
	private HashGenerator hashGen;
	
	private EncryptionKeyDAO keyDao;
	private InvalidTokenDAO invalidDao;
	private EntityBuilder<InvalidLToken> invalidBuilder;
	
	private LTokenConfiguration config;
	
	public LTokenManagerImpl(EntityBuilder<InvalidLToken> invalidBuilder, TokenCipher cipher, TokenDecipher decipher, TokenSerializer serializer, HashGenerator hashGen,
			EncryptionKeyDAO keyDao, InvalidTokenDAO invalidDao, LTokenConfiguration config) {
		this.invalidBuilder = invalidBuilder;
		this.cipher = cipher;
		this.decipher = decipher;
		this.serializer = serializer;
		this.hashGen = hashGen;
		this.keyDao = keyDao;
		this.invalidDao = invalidDao;
		this.config = config;
	}


	public String generateToken(LTokenInfo tokenInfo) throws TokenGenerationException {
		EncryptionKey key;
		try {
			key = resolveKeyForToday();
		} catch (KeyResolutionException e) {
			throw new TokenGenerationException(e);
		}
		LTokenData tokenData = cipher.cipher(tokenInfo, key, new LocalDateTime());
		return serializer.serialize(tokenData);
	}


	public LTokenInfo authenticate(String token) throws TokenAuthenticationException, TokenDecipherException {
		if(isTokenInvalidated(token)) throw new TokenAuthenticationException("Token was invalidated");
		
		LTokenData tokenData = serializer.deserialize(token);
		
		EncryptionKey key;
		try {
			key = resolveKeyForTimeStamp(tokenData.getTimeStamp());
		} catch (KeyResolutionException e) {
			throw new TokenAuthenticationException(e);
		}
		
		return decipher.decipher(tokenData, key);
	}

	public boolean needsNewToken(String token) throws MalformedTokenException, BadTokenVersionException {
		int regenDays = config.getTokenRegenerationNotification();
		LocalDateTime regenLimit = new LocalDate().minusDays(regenDays).toLocalDateTime(new LocalTime());
		
		LTokenData tokenData = serializer.deserialize(token);
		LocalDateTime tokenTime = tokenData.getTimeStamp();
		
		
		return tokenTime.isBefore(regenLimit);
	}

	public void invalidateToken(String token) throws TokenAuthenticationException, MalformedTokenException, BadTokenVersionException, DAOOperationException {
		if(isTokenInvalidated(token)){
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("Invalid token rejected : " + token);
			}
		}
		
		LTokenData tokenData = serializer.deserialize(token);
		
		String hash = hashGen.hash(token);
		LocalDate day = tokenData.getTimeStamp().toLocalDate();
		
		InvalidLToken invalidToken = invalidBuilder.instantiate();
		invalidToken.setDay(day);
		invalidToken.setHash(hash);
		
		invalidDao.insertInvalidToken(invalidToken);
	}
	
	private EncryptionKey resolveKeyForTimeStamp(LocalDateTime timeStamp) throws KeyResolutionException {
		LocalDate day = timeStamp.toLocalDate();
		return resolveKeyForDay(day);
	}
	
	private EncryptionKey resolveKeyForToday() throws KeyResolutionException {
		LocalDate day = new LocalDate();
		return resolveKeyForDay(day);
	}
	
	private EncryptionKey resolveKeyForDay(LocalDate day) throws KeyResolutionException{
		EncryptionKey key;
		try {
			key = keyDao.getKeyForDayOrNull(day);
		} catch (DAOOperationException e) {
			throw new KeyResolutionException("Error while retrieving encryption key", e);
		}
		
		if(key==null) throw new KeyResolutionException("No key found for day " + day);
		else return key;
	}
	
	private boolean isTokenInvalidated(String token) throws TokenAuthenticationException{
		String hash = hashGen.hash(token);
		try {
			return invalidDao.existsByHash(hash);
		} catch (DAOOperationException e) {
			throw new TokenAuthenticationException("Could not verify if token was invalidated", e);
		}
	}
	
	//TODO sortir de là
	private class KeyResolutionException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7119796535810808205L;

		private KeyResolutionException(String message, Throwable cause) {
			super(message, cause);
		}

		private KeyResolutionException(String message) {
			super(message);
		}
		
		
		
	}
}
