package free.lighttokens.core.util.secret;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import free.lighttokens.exceptions.InvalidTokenDataException;

public class SecretPartsEncoderTest {

	private SecretPartsEncoder testee = new SecretPartsEncoder();
	
	@Test
	public final void testGetUserIdentifierLengthBytes() throws InvalidTokenDataException {
		byte[] result = testee.getUserIdentifierLengthBytes("Kant");
		ByteBuffer bufferResult = ByteBuffer.wrap(result);
		
		short uil = bufferResult.getShort();
		assertEquals(4,uil);
	}

	@Test
	public final void testGetTimeStampBytes() {
		long ts = 12345678901L;
		LocalDateTime time = new LocalDateTime(ts, DateTimeZone.UTC);
		
		byte[] result = testee.getTimeStampBytes(time);
		ByteBuffer bufferResult = ByteBuffer.wrap(result);
		
		long resultTs = bufferResult.getLong();
		assertEquals(ts, resultTs);
	}

	@Test
	public final void testGetStringBytes() {
		String input = "hhhhh";
		
		byte[] result = testee.getStringBytes(input);
		String output = new String(result, SecretDataEncoder.CHARSET);
		
		assertEquals(input, output);
	}

	@Test
	public final void testGetRandomBytes() {
		int length = 15;
		
		byte[] result = testee.getRandomBytes(length);
		
		assertEquals(length, result.length);
	}

}
