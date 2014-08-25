package free.lighttokens.core.util;

import static org.junit.Assert.*;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

public class TimestampConvertorUtilTest {

	@Test
	public final void testToMillis() {
		long someTime = 123465789012L;
		LocalDateTime time = new LocalDateTime(someTime, DateTimeZone.UTC);
		
		long result = TimestampConvertorUtil.toMillis(time);
		
		assertEquals(someTime, result);
	}

	@Test
	public final void testToDateTime() {
		String tsString = "123456789012";
		
		LocalDateTime result = TimestampConvertorUtil.toDateTime(tsString);
		
		assertEquals(new LocalDateTime(123456789012L, DateTimeZone.UTC), result);
	}

	@Test
	public final void testToStringLocalDateTime() {
		long someTime = 123465789012L;
		LocalDateTime time = new LocalDateTime(someTime, DateTimeZone.UTC);

		String result = TimestampConvertorUtil.toString(time);
		
		assertEquals("123465789012", result); 
		
	}

}
