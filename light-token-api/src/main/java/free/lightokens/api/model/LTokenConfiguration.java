package free.lightokens.api.model;

public class LTokenConfiguration {

	private int tokenValidity;
	private int tokenRegenerationNotification;
	private int futureKeys;
	private String defaultAlgorithm;
	private int minSecretDataLength;
	
	public LTokenConfiguration() {	}
	
	/**
	 * 
	 * @param tokenValidity number of days
	 * @param tokenRegenerationNotification age of a token in days when the token is considered as old and a new token is encouraged to be generated
	 * @param futureKeys number of keys generated in the future
	 * @param defaultAlgorithm the algorithm used to cipher the secret data
	 * @param minSecretDataLength the minimum secret data length in termes of bytes to encode
	 */
	public LTokenConfiguration(int tokenValidity, int tokenRegenerationNotification, int futureKeys, String defaultAlgorithm,
			int minSecretDataLength) {
		super();
		this.tokenValidity = tokenValidity;
		this.tokenRegenerationNotification = tokenRegenerationNotification;
		this.futureKeys = futureKeys;
		this.defaultAlgorithm = defaultAlgorithm;
		this.minSecretDataLength = minSecretDataLength;
	}



	public int getMinSecretDataLength() {
		return minSecretDataLength;
	}
	public void setMinSecretDataLength(int minSecretDataLength) {
		this.minSecretDataLength = minSecretDataLength;
	}
	public int getTokenValidity() {
		return tokenValidity;
	}
	public void setTokenValidity(int tokenValidity) {
		this.tokenValidity = tokenValidity;
	}
	public int getTokenRegenerationNotification() {
		return tokenRegenerationNotification;
	}
	public void setTokenRegenerationNotification(int tokenRegenerationNotification) {
		this.tokenRegenerationNotification = tokenRegenerationNotification;
	}
	public int getFutureKeys() {
		return futureKeys;
	}
	public void setFutureKeys(int futureKeys) {
		this.futureKeys = futureKeys;
	}
	public String getDefaultAlgorithm() {
		return defaultAlgorithm;
	}
	public void setDefaultAlgorithm(String defaultAlgorithm) {
		this.defaultAlgorithm = defaultAlgorithm;
	}
	
	
	
}
