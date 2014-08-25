package free.lighttokens.core.util;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

public class TimestampConvertorUtil {

	public static long toMillis(LocalDateTime time){
		return time.toDateTime(DateTimeZone.UTC).getMillis();
	}
	
	static LocalDateTime toDateTime(String tsString) {
		long ts = Long.valueOf(tsString);
		return new LocalDateTime(ts, DateTimeZone.UTC);
	}
	
	public static String toString(LocalDateTime timeStamp) {
		long ts = timeStamp.toDateTime(DateTimeZone.UTC).getMillis();
		return String.valueOf(ts);
	}

}
