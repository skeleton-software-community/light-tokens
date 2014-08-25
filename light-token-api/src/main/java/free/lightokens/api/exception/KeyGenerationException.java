package free.lightokens.api.exception;

public class KeyGenerationException extends EncryptionKeyOperationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7997189593497295666L;

	public KeyGenerationException(String message, Exception cause) {
		super(message, cause);
	}

}
