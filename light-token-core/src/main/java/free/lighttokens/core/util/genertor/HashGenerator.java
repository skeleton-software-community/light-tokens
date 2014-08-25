package free.lighttokens.core.util.genertor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * Generates hashes of strings
 * 
 * The SHA-1 algorithm musts be supported. It is provided by the SunJCE provider.
 * 
 * @see http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
 * @author Mounir Regragui
 *
 */
public class HashGenerator {

	private static final String ALGORITHM = "SHA-1";

	/**
	 * @throws RuntimeException if the SHA-1 algorithm is not supported
	 */
	public HashGenerator() {
			generateDigest(); //Validation that the algorithm is supported
	}
	
	/**
	 * Generate a SHA-1 hash of a string
	 * 
	 * @param input a non null string to be hashed
	 * @return the hash
	 */
	public String hash(String input){
		MessageDigest digest = generateDigest();
		
		byte[] inputBytes = Base64.decodeBase64(input);
		byte[] hashBytes = digest.digest(inputBytes);
		return Base64.encodeBase64String(hashBytes);
	}
	
	private MessageDigest generateDigest() {
		try {
			return MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algorithm " + ALGORITHM + " is not supported");
		}
	}
}
