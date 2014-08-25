package free.lighttokens.exceptions;

import free.lightokens.api.exception.TokenDecipherException;

public class BadTokenVersionException extends TokenDecipherException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7561806003555058986L;

	public BadTokenVersionException(String version) {
		super("Unsupported token version : " + version);
	}

}
