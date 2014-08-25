package free.lighttokens.core.util.secret;

import java.nio.ByteBuffer;

import com.google.common.primitives.Longs;

import free.lighttokens.core.model.SecretData;
import free.lighttokens.exceptions.InvalidTokenDataException;

/**
 * Decodes secret data
 * 
 * The format of secretData
 * USERNAME_LENGTH TIMESTAMP USERNAME RANDOM_DATA
 * 
 * @author Mounir Regragui
 * @see SecretDataEncoder
 */
public class SecretDataDecoder {

	
/**
 * Decodes a secret data encoded in a byte array
 * @param secretDataBytes 
 * @return
 * @throws InvalidTokenDataException if the encoded byte array is not valid
 */
	public SecretData resolveSecretData(byte[] secretDataBytes) throws InvalidTokenDataException {
		
		ByteBuffer sdBuffer = ByteBuffer.wrap(secretDataBytes);
		short userIdLength = sdBuffer.getShort();
		long secretTs = decodeTimestamp(sdBuffer);
		String userId = decodeUserIdentifier(sdBuffer, userIdLength);
		
		SecretData result = new SecretData();
		result.setTimestamp(secretTs);
		result.setUserIdentifier(userId);
		
		return result;
	}
	
	private String decodeUserIdentifier(ByteBuffer sdBuffer, short userIdLength) throws InvalidTokenDataException {
		if(userIdLength<0) throw new InvalidTokenDataException("Invalid user identifier length " + userIdLength);
		byte[] userIdBytes = getNextBytes(sdBuffer, userIdLength);
		
		return new String(userIdBytes, SecretDataEncoder.CHARSET);
	}

	private long decodeTimestamp(ByteBuffer sdBuffer) {
		byte[] tsBytes = getNextBytes(sdBuffer, SecretDataEncoder.TIMESTAMP_SIZE);
		return Longs.fromByteArray(tsBytes);
	}

	private byte[] getNextBytes(ByteBuffer buffer, int length){
		byte[] result = new byte[length];
		buffer.get(result, 0, length);
		return result;
	}

	
}
