package free.lighttokens.core.util.cipher;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.LocalDateTime;

import free.lightokens.api.exception.TokenDecipherException;
import free.lightokens.api.model.LTokenInfo;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.core.model.SecretData;
import free.lighttokens.core.util.CipherBuilder;
import free.lighttokens.core.util.TimestampConvertorUtil;
import free.lighttokens.core.util.secret.SecretDataDecoder;
import free.lighttokens.exceptions.InvalidTokenDataException;

/**
 * Deciphers {@link LTokenData} to {@link LTokenInfo}
 * @author Mounir Regragui
 *
 */
public class TokenDecipher {

	private SecretDataDecoder sdEncoder;
	private String supportedVersion;
	
	/**
	 * 
	 * @param sdEncoder not null
	 * @param supportedVersion the version of tokens it can deciphers, not null
	 */
	public TokenDecipher(SecretDataDecoder sdEncoder, String supportedVersion) {
		super();
		this.sdEncoder = sdEncoder;
		this.supportedVersion = supportedVersion;
	}

	/**
	 * Deciphers a token data to produce token information
	 * @param tokenData to decipher
	 * @param key to decipher
	 * @return the token information
	 * @throws TokenDecipherException if one of the data or the key is invalid   
	 */
	public LTokenInfo decipher(LTokenData tokenData, EncryptionKey key) throws TokenDecipherException {
		validateVersion(tokenData.getVersion());
		String cipheredData = tokenData.getCipheredData();
		
		Cipher cipher;
		try {
			cipher = CipherBuilder.createCipher(key, Cipher.DECRYPT_MODE);
		} catch (GeneralSecurityException e) {
			throw new TokenDecipherException(e);
		}

		byte[] cipheredBytes = Base64.decodeBase64(cipheredData);
		byte[] secretDataBytes = decipherData(cipheredBytes, cipher);
		SecretData secretData;
		try {
			secretData = sdEncoder.resolveSecretData(secretDataBytes);
		} catch (InvalidTokenDataException e) {
			throw new TokenDecipherException(e);
		}
		validateTimestamps(secretData, tokenData.getTimeStamp());
		
		LTokenInfo result = new LTokenInfo();
		result.setUserIdentifier(secretData.getUserIdentifier());
		return result;
	}
	
	private void validateVersion(String version) throws TokenDecipherException {
		if(!supportedVersion.equals(version)) throw new TokenDecipherException("Unsupported token version");
	}

	private void validateTimestamps(SecretData secretData, LocalDateTime clearTimeStamp) throws TokenDecipherException {
		if(!isTimestampOk(secretData, clearTimeStamp)) throw new TokenDecipherException("Timestamps do not check");
	}
	
	private boolean isTimestampOk(SecretData secretData, LocalDateTime clearTimeStamp) {
		return secretData.getTimestamp() == TimestampConvertorUtil.toMillis(clearTimeStamp);
		
	}

	private byte[] decipherData(byte[] cipheredBytes, Cipher cipher) throws TokenDecipherException {
		try {
			return cipher.doFinal(cipheredBytes);
		} catch (GeneralSecurityException e) {
			throw new TokenDecipherException(e);
		}
	}
	
}
 