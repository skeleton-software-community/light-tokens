package free.lighttokens.core.util;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import free.lightokens.api.model.entity.EncryptionKey;
/**
 * Builds {@link Cipher}s
 * @author Mounir Regragui
 *
 */
public class CipherBuilder {

	/**
	 * Creates a new {@link Cipher} initialized with a the requested mode and the key
	 * The opMode value determines the mode of the Cipher. Values are held by the {@link Cipher} class
	 * <ul>
	 * <li> {@link Cipher#ENCRYPT_MODE} </li>
	 * <li> {@link Cipher#DECRYPT_MODE} </li>
	 * <li> {@link Cipher#WRAP_MODE} </li>
	 * <li> {@link Cipher#UNWRAP_MODE} </li>
	 * </ul>
	 * 
	 * @see Cipher#getInstance(String)
	 * 
	 * @param key the encryption key of the Cipher
	 * @param opMode a valid opMode for a Cipher
	 * @return a fresh instance of {@link Cipher} initialized in the requested mode and with the supplied key
	 * @throws GeneralSecurityException if the key's algorithm is not supported
	 */
	public static Cipher createCipher(EncryptionKey key, int opMode) throws GeneralSecurityException{
		SecretKeySpec stdKey = createStandardKey(key);
		Cipher cipher = Cipher.getInstance(key.getAlgorithm());
		cipher.init(opMode, stdKey);
		return cipher;
	
	}
	
	private static SecretKeySpec createStandardKey(EncryptionKey key) {
		byte[] encodedPhrase = Base64.decodeBase64(key.getPhrase());
		return new SecretKeySpec(encodedPhrase, key.getAlgorithm());
	}
	
}
