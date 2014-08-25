package free.lighttokens.exceptions;

import free.lightokens.api.exception.TokenDecipherException;

public class MalformedTokenException extends TokenDecipherException{

	public MalformedTokenException(String message) {
		super(message);
	}
	
	

	public MalformedTokenException(String message, Exception cause) {
		super(message, cause);
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -8258660695453709739L;

}
