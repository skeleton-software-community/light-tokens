package free.lighttokens.core.util.secret;

import java.nio.ByteBuffer;
import java.util.Random;

import org.joda.time.LocalDateTime;

import com.google.common.primitives.Longs;

import free.lighttokens.core.util.TimestampConvertorUtil;
import free.lighttokens.exceptions.InvalidTokenDataException;

/**
 * Encodes parts of secret data
 * @author Mounir Regragui
 *
 */
public class SecretPartsEncoder {

	private Random random;
	
	public SecretPartsEncoder() {
		super();
		this.random = new Random();
	}

	/**
	 * 
	 * @param userIdentifier not null
	 * @return
	 * @throws InvalidTokenDataException if user identifier is longer than {@link Short#MAX_VALUE}. <br/>That's a very long name
	 */
	public byte[] getUserIdentifierLengthBytes(String userIdentifier) throws InvalidTokenDataException{
		short length = getUsernameLength(userIdentifier);
		
		return ByteBuffer.allocate(SecretDataEncoder.USERID_LENGTH_SIZE).putShort(length).array();
	}

	private short getUsernameLength(String username)
			throws InvalidTokenDataException {
		int lengthInt = username.length();
		if(lengthInt>Short.MAX_VALUE) throw new InvalidTokenDataException("User identifier is too long");
		
		short length = (short) lengthInt;
		return length;
	}
	
	/**
	 * 
	 * @param timeStamp not null
	 * @return
	 */
	public byte[] getTimeStampBytes(LocalDateTime timeStamp){
		long ts = TimestampConvertorUtil.toMillis(timeStamp);
		return Longs.toByteArray(ts);
	}
	
	/**
	 * 
	 * @param input not null
	 * @return
	 */
	public byte[] getStringBytes(String input){
		return input.getBytes(SecretDataEncoder.CHARSET);
	}
	
	/**
	 * 
	 * @param length greater than 0
	 * @return
	 */
	public byte[] getRandomBytes(int length){
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return randomBytes;
	}
}
