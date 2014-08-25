package free.lighttokens.core.util;

import org.joda.time.LocalDateTime;

import free.lighttokens.core.ImplementationVersion;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.exceptions.BadTokenVersionException;
import free.lighttokens.exceptions.MalformedTokenException;

/**
 * The format of tokens is
 * <pre>version;timestamp;cipheredData</pre>
 * 
 * All times are in the UTC time zone
 * @author Mounir Regragui
 *
 */
public class TokenSerializer {

	public String serialize(LTokenData tokenData){
		return tokenData.getVersion() + ';' + TimestampConvertorUtil.toString(tokenData.getTimeStamp()) + ';' + tokenData.getCipheredData();
	}
	
	public LTokenData deserialize(String token) throws MalformedTokenException, BadTokenVersionException{
		String[] parts = token.split(";", 3);
		
		validateToken(parts);
		
		LocalDateTime timeStamp = TimestampConvertorUtil.toDateTime(parts[1]);
		
		return new LTokenData(parts[2], timeStamp, parts[0]);
	}

	private void validateVersion(String suppliedVersion) throws BadTokenVersionException {
		if(!ImplementationVersion.IMPL_VERSION.equals(suppliedVersion)){
			throw new BadTokenVersionException(suppliedVersion);
		}
	}

	private void validateToken(String[] parts) throws MalformedTokenException, BadTokenVersionException {
		if(parts.length<3) throw new MalformedTokenException("Token has less than 3 parts");
		validateVersion(parts[0]);
		
		
		if(parts.length!=3) throw new MalformedTokenException("Token does not have 3 parts");
		
		try{
			Long.valueOf(parts[1]);			
		}catch(NumberFormatException e){
			throw new MalformedTokenException("Invalid timestamp", e);
		}
		
	}
	
}
