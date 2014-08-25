package free.lighttokens.core.util.secret;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import free.lightokens.api.model.LTokenInfo;
import free.lighttokens.core.model.SecretData;
import free.lighttokens.core.util.TimestampConvertorUtil;
import free.lighttokens.exceptions.InvalidTokenDataException;

public class SecretDataDecoderTest {

	private SecretDataDecoder testee = new SecretDataDecoder();
	
	@Test
	public final void testResolveSecretWithoutRandom() throws InvalidTokenDataException {
		LocalDateTime time = new LocalDateTime();
		byte[] encoded = generateEncodedData(0, time);
		
		SecretData result = testee.resolveSecretData(encoded);
		
		assertEquals(TimestampConvertorUtil.toMillis(time), result.getTimestamp());
		assertEquals("Kant", result.getUserIdentifier());
	}
	
	@Test
	public final void testResolveSecretWithRandom() throws InvalidTokenDataException {
		LocalDateTime time = new LocalDateTime();
		byte[] encoded = generateEncodedData(5000, time);
		
		SecretData result = testee.resolveSecretData(encoded);
		
		assertEquals(TimestampConvertorUtil.toMillis(time), result.getTimestamp());
		assertEquals("Kant", result.getUserIdentifier());
	}
	
	private byte[] generateEncodedData(int msdl, LocalDateTime timeStamp){
		SecretPartsEncoder partsEncoder = new SecretPartsEncoder();
		SecretDataEncoder encoder = new SecretDataEncoder(partsEncoder, msdl);
		
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		
		try {
			return encoder.computeSecretDataBytes(tokenInfo, timeStamp);
		} catch (InvalidTokenDataException e) {
			throw new RuntimeException(e);
		}
	}


}
