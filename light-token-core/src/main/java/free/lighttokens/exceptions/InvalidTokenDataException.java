package free.lighttokens.exceptions;

import free.lightokens.api.exception.TokenGenerationException;

public class InvalidTokenDataException extends TokenGenerationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044960498160536353L;

	public InvalidTokenDataException(String message) {
		super(message);
	}

}
