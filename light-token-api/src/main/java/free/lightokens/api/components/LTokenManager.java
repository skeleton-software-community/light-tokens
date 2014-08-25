package free.lightokens.api.components;

import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.exception.TokenAuthenticationException;
import free.lightokens.api.exception.TokenDecipherException;
import free.lightokens.api.exception.TokenGenerationException;
import free.lightokens.api.model.LTokenInfo;

public interface LTokenManager {

	String generateToken(LTokenInfo tokenInfo) throws TokenGenerationException;
	
	LTokenInfo authenticate(String token) throws TokenDecipherException, TokenAuthenticationException;
	boolean needsNewToken(String token) throws TokenDecipherException;
	
	void invalidateToken(String token) throws TokenAuthenticationException, TokenDecipherException, DAOOperationException;
}
