    package free.lighttokens.core.util.cipher;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.LocalDateTime;

import free.lightokens.api.exception.TokenGenerationException;
import free.lightokens.api.model.LTokenInfo;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.core.util.CipherBuilder;
import free.lighttokens.core.util.secret.SecretDataEncoder;

/**
 * Ciphers {@link LTokenInfo} to {@link LTokenData}
 * @author Mounir Regragui
 *
 */
public class TokenCipher {

	private SecretDataEncoder sdEncoder;
	private String version;

	/**
	 * 
	 * @param sdEncoder not null
	 * @param version not null
	 */
	public TokenCipher(SecretDataEncoder sdEncoder, String version) {
		super();
		this.sdEncoder = sdEncoder;
		this.version = version;
	}

	/**
	 * Ciphers token informations to produce a token data
	 * @param tokenInfo informations to be ciphered
	 * @param key to cipher
	 * @param timeStamp timestamp of the token generation
	 * @return the token data
	 * @throws TokenGenerationException if the key has a bad phrase or bad algorithm
	 */
	public LTokenData cipher(LTokenInfo tokenInfo, EncryptionKey key, LocalDateTime timeStamp) throws TokenGenerationException {

		Cipher cipher;
		try {
			cipher = CipherBuilder.createCipher(key, Cipher.ENCRYPT_MODE);
		} catch (GeneralSecurityException e) {
			throw new TokenGenerationException(e);
		}
		
		byte[] secretDataBytes = sdEncoder.computeSecretDataBytes(tokenInfo, timeStamp);
		byte[] cipheredBytes = cipherBytes(secretDataBytes, cipher);
		String cipheredText = Base64.encodeBase64String(cipheredBytes);

		return new LTokenData(cipheredText, timeStamp, version);
	}

	private byte[] cipherBytes(byte[] secretDataBytes, Cipher cipher) throws TokenGenerationException {
		try {
			return cipher.doFinal(secretDataBytes);
		} catch (GeneralSecurityException e) {
			throw new TokenGenerationException(e);
		}
	}




}
