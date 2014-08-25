package free.lighttokens.core.factory;

import free.lightokens.api.model.LTokenConfiguration;

/**
 * Builds {@link LTokenConfiguration}s
 * @author Mounir Regragui
 *
 */
public class DefaultConfigurationBuilder {

	private static final String DEFAULT_ALGO = "AES";
	private static final int DEFAULT_MIN_SDL = 50;

	/**
	 * Builds a configuration with the AES algorithm and 50 as minimum secret data length
	 * @param tokenValidity number of days
	 * @param tokenRegenerationNotification age of a token in days when the token is considered as old and a new token is encouraged to be generated
	 * @param futureKeys number of keys generated in the future
	 * @return the created configuration
	 */
	public static LTokenConfiguration buildWithDefaults(int tokenValidity, int futureKeys, int tokenRegenerationNotification){
		return new LTokenConfiguration(tokenValidity, tokenRegenerationNotification, futureKeys, DEFAULT_ALGO, DEFAULT_MIN_SDL);
	}
}
