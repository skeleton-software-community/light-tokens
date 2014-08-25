package free.lighttokens.core.util.genertor;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

import free.lightokens.api.exception.KeyGenerationException;

/**
 * Generates key phrases
 * @author Mounir Regragui
 *
 */
public class PhraseGenerator {

	private static final int KEY_SIZE = 128;

	/**
	 * Generates a key for a particluar algorithm
	 * 
	 * @param algorithm a non null string representing an algorithm name
	 * @return the key phrase generated
	 * @throws KeyGenerationException if the algorithm is not supported by the platform
	 */
	public String generatePhrase(String algorithm) throws KeyGenerationException{
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new KeyGenerationException("Algorithm " + algorithm + " is not supported by the platform", e);
		}
		keyGen.init(KEY_SIZE);
		SecretKey secretKey = keyGen.generateKey();
		byte[] encodedPhrase = secretKey.getEncoded();
		return Base64.encodeBase64String(encodedPhrase);
	}
}
