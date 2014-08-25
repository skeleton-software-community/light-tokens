package free.lightokens.api.exception;

public class TokenAuthenticationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -904371641858418026L;

	public TokenAuthenticationException(String message, Exception cause) {
		super(message, cause);
	}
	
	public TokenAuthenticationException(String message){
		super(message);
	}

	public TokenAuthenticationException(Exception e) {
		super(e);
	}
}
