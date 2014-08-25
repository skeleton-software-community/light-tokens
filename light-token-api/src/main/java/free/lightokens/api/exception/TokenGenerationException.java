package free.lightokens.api.exception;

public class TokenGenerationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9199918693070858749L;

	public TokenGenerationException(String message) {
		super(message);
	}

	public TokenGenerationException(String message, Exception e) {
		super(message, e);
	}

	public TokenGenerationException(Exception e) {
		super(e);
	}

}
