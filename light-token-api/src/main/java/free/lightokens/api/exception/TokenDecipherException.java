package free.lightokens.api.exception;


public class TokenDecipherException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4274739744933803849L;

	public TokenDecipherException(String message, Exception cause) {
		super(message, cause);
	}
	
	public TokenDecipherException(String message){
		super(message);
	}

	public TokenDecipherException(Exception e) {
		super(e);
	}
}
