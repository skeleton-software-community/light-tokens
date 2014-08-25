package free.lighttokens.core.components;

import java.util.Collection;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import free.lightokens.api.builder.EntityBuilder;
import free.lightokens.api.components.BatchPerformer;
import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.exception.EncryptionKeyOperationException;
import free.lightokens.api.exception.KeyGenerationException;
import free.lightokens.api.model.LTokenConfiguration;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lightokens.api.model.entity.InvalidLToken;
import free.lighttokens.core.LoggerNames;
import free.lighttokens.core.util.AlgorithmResolver;
import free.lighttokens.core.util.genertor.PhraseGenerator;

public class BatchPerformerImpl implements BatchPerformer{

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerNames.BATCH_LOGGER);

	private EncryptionKeyDAO keyDao;
	private InvalidTokenDAO invalidDao;
	private LTokenConfiguration config;
	private AlgorithmResolver algoResolver;
	private PhraseGenerator phraseGenerator;
	private EntityBuilder<EncryptionKey> keyBuilder;

	public BatchPerformerImpl(EncryptionKeyDAO keyDao, InvalidTokenDAO invalidDao, EntityBuilder<EncryptionKey> keyBuilder,
			LTokenConfiguration config, AlgorithmResolver algoResolver,
			PhraseGenerator phraseGenerator) {
		super();
		this.keyDao = keyDao;
		this.invalidDao = invalidDao;
		this.keyBuilder = keyBuilder;
		this.config = config;
		this.algoResolver = algoResolver;
		this.phraseGenerator = phraseGenerator;
	}

	@Override
	public void cleanOldKeys() throws EncryptionKeyOperationException {
		int tokenValidity = config.getTokenValidity();
		LocalDate limitDay = new LocalDate().minusDays(tokenValidity);

		Collection<EncryptionKey> oldKeys;
		try {
			oldKeys = keyDao.findAllBefore(limitDay);
		} catch (DAOOperationException e) {
			throw new EncryptionKeyOperationException("Could not find keys before " + limitDay.toString(), e);
		}

		for(EncryptionKey ok : oldKeys){
			try {
				keyDao.deleteKeyForDay(ok.getDay());
			} catch (DAOOperationException e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("Could not delete an old key for " + ok.getDay(), e);
				}
			}
		}
	}

	@Override
	public void generateKeys() throws EncryptionKeyOperationException {
		int futureKeys = config.getFutureKeys();

		try {
			for(int i=1;i<=futureKeys;i++){
				generateKeyForFutureDay(i);
			}
		} catch (DAOOperationException e) {
			throw new EncryptionKeyOperationException("Error generating keys", e);
		}

	}

	private void generateKeyForFutureDay(int dayOffset) throws DAOOperationException, KeyGenerationException {
		LocalDate day = new LocalDate().plusDays(dayOffset);

		if(!existsKeyForDay(day)){

			String algorithm = algoResolver.resolveCipherAlgorithm();
			String phrase = phraseGenerator.generatePhrase(algorithm);

			EncryptionKey eKey = keyBuilder.instantiate();
			eKey.setAlgorithm(algorithm);
			eKey.setDay(day);
			eKey.setPhrase(phrase);

			keyDao.insertKey(eKey);
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("Inserted key for " + day);
			}
		}else{
			if(LOGGER.isWarnEnabled()){
				LOGGER.warn("A key already exists for " + day);
			}
		}

	}

	private boolean existsKeyForDay(LocalDate day) throws DAOOperationException {
		return keyDao.getKeyForDayOrNull(day)!=null;
	}

	@Override
	public void cleanOldInvalidTokens() throws EncryptionKeyOperationException {
		int tokenValidity = config.getTokenValidity();
		LocalDate limitDay = new LocalDate().minusDays(tokenValidity);

		Collection<InvalidLToken> oldTokens;
		try {
			oldTokens = invalidDao.findAllBefore(limitDay);
		} catch (DAOOperationException e) {
			throw new EncryptionKeyOperationException("Could not find tokens before " + limitDay.toString(), e);
		}

		for(InvalidLToken ot : oldTokens){
			try {
				invalidDao.deleteInvalidToken(ot);
			} catch (DAOOperationException e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("An invalid token could not be deleted", e);
				}
			}
		}
	}

}
