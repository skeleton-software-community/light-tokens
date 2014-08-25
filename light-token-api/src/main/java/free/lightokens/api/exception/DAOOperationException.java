package free.lightokens.api.exception;

public class DAOOperationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2797395362144818317L;

	public DAOOperationException(Exception cause) {
		super(cause);
	}

	public DAOOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOOperationException(String message) {
		super(message);
	}
	
	
	
}
