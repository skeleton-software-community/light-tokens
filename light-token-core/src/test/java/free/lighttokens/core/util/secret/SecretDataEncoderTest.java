package free.lighttokens.core.util.secret;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import free.lightokens.api.model.LTokenInfo;
import free.lighttokens.exceptions.InvalidTokenDataException;

public class SecretDataEncoderTest {

	
	@Test
	public final void validateComputeSecretDataWithoutRandom() throws InvalidTokenDataException {
		SecretDataEncoder testee = generateTestee(0);
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		LocalDateTime timeStamp = new LocalDateTime();
		
		testee.computeSecretDataBytes(tokenInfo, timeStamp);
	}

	@Test
	public final void validateComputeSecretDataWithRandom() throws InvalidTokenDataException {
		SecretDataEncoder testee = generateTestee(5000);
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		LocalDateTime timeStamp = new LocalDateTime();
		
		testee.computeSecretDataBytes(tokenInfo, timeStamp);
	}
	
	
	private SecretDataEncoder generateTestee(int msdl){
		SecretPartsEncoder partsEncoder = new SecretPartsEncoder();
		return new SecretDataEncoder(partsEncoder, msdl);
	}

}
