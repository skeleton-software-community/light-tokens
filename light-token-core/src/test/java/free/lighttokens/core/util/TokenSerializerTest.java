package free.lighttokens.core.util;

import static org.junit.Assert.*;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import free.lighttokens.core.ImplementationVersion;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.exceptions.BadTokenVersionException;
import free.lighttokens.exceptions.MalformedTokenException;

public class TokenSerializerTest {
	
	private TokenSerializer testee = new TokenSerializer();

	@Test
	public final void testSerialize() {
		LocalDateTime time = new LocalDateTime();
		LTokenData tokenData = new LTokenData("abc", time, "1");
		String result = testee.serialize(tokenData);
		
		String expectedResult = "1;" + TimestampConvertorUtil.toString(time) + ";abc";
		
		assertEquals(expectedResult, result);
	}

	@Test
	public final void testDeserialize() throws MalformedTokenException, BadTokenVersionException {
		LocalDateTime time = new LocalDateTime();
		String token = ImplementationVersion.IMPL_VERSION + ';' + TimestampConvertorUtil.toString(time) + ";abc";
		
		LTokenData result = testee.deserialize(token);
		assertEquals(time, result.getTimeStamp());
		assertEquals("abc", result.getCipheredData());
	}
	
	@Test(expected=MalformedTokenException.class)
	public void testMalformedToken1() throws MalformedTokenException, BadTokenVersionException {
		String token = "hhhhhh";
		testee.deserialize(token);
	}
	
	@Test(expected=MalformedTokenException.class)
	public void testMalformedToken2() throws MalformedTokenException, BadTokenVersionException {
		String token = ImplementationVersion.IMPL_VERSION + ';' + "hhhhhh";
		testee.deserialize(token);
	}
	
	@Test(expected=MalformedTokenException.class)
	public void testMalformedToken3() throws MalformedTokenException, BadTokenVersionException {
		String token = ImplementationVersion.IMPL_VERSION + ';' + "a;hhhhhh";
		testee.deserialize(token);
	}
	

	@Test(expected=BadTokenVersionException.class)
	public void testBadTokenVersion() throws MalformedTokenException, BadTokenVersionException {
		String token = "hhhh" + ";a;abc";
		testee.deserialize(token);
	}

}
