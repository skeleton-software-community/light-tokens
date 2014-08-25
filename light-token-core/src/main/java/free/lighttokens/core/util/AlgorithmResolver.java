package free.lighttokens.core.util;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * Resolves supported algorithms
 * 
 * The AES algorithm must be supported. It is provided by the SunJCE provider.
 * 
 * @see http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
 * @author Mounir Regragui
 *
 */
public class AlgorithmResolver {

	private static final String DEFAULT_CIPHER_ALGO = "AES";
	private String favoriteEncAlgorithm;
	
	/**
	 * 
	 * @param favoriteEncAlgorithm an algorithm to use for encryption
	 * @throws RuntimeException if both AES and favoriteEncAlgorithm are not supported
	 */
	public AlgorithmResolver(String favoriteEncAlgorithm) {
		super();
		this.favoriteEncAlgorithm = favoriteEncAlgorithm;
		validateDefaultAlgo();
	}

	private void validateDefaultAlgo() {
		try {
			Cipher.getInstance(DEFAULT_CIPHER_ALGO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			try {
				Cipher.getInstance(favoriteEncAlgorithm);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
				throw new RuntimeException("Neither " + DEFAULT_CIPHER_ALGO + " or " + favoriteEncAlgorithm +
						" are supported by the platform");
			}
		}
	}

	/**
	 * Resolves a suitable algorithm for {@link Cipher}s
	 * @return the algorithm
	 */
	public String resolveCipherAlgorithm(){
		try {
			Cipher.getInstance(favoriteEncAlgorithm);
			return favoriteEncAlgorithm;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			return DEFAULT_CIPHER_ALGO;
		}
	}
	
}
