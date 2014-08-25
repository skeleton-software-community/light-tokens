package free.lighttokens.core.util.secret;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.joda.time.LocalDateTime;

import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

import free.lightokens.api.model.LTokenInfo;
import free.lighttokens.exceptions.InvalidTokenDataException;

/**
 * Encodes secret data
 * 
 * The encoder is configured with a minimum secret data length. If the
 * length of the encoded data is lesser than this minimum, random data
 * will be appended in order for the encoded data to have its minimum
 * size.
 * 
 * The format of secretData is
 * USERNAME_LENGTH TIMESTAMP USERNAME RANDOM_DATA
 * 
 * @author Mounir Regragui
 *
 */
public class SecretDataEncoder {

	static final int TIMESTAMP_SIZE = Longs.BYTES;
	static final int USERID_LENGTH_SIZE = Shorts.BYTES;
	/**
	 * The charset used to encode and decode user identifiers
	 */
	static final Charset CHARSET = Charset.forName("UTF-8");
	
	private SecretPartsEncoder partsEncoder;
	private int minSecretDataLength;
	
	/**
	 * @param partsEncoder not null
	 * @param minSecretDataLength the minimum secret data length
	 */
	public SecretDataEncoder(SecretPartsEncoder partsEncoder, int minSecretDataLength) {
		super();
		this.partsEncoder = partsEncoder;
		this.minSecretDataLength = minSecretDataLength;
	}

	public byte[] computeSecretDataBytes(LTokenInfo tokenInfo, LocalDateTime timeStamp) throws InvalidTokenDataException {
		String userIdentifier = tokenInfo.getUserIdentifier();

		byte[] userIdLengthBytes = partsEncoder.getUserIdentifierLengthBytes(userIdentifier);
		byte[] tsBytes = partsEncoder.getTimeStampBytes(timeStamp);
		byte[] userIdBytes = partsEncoder.getStringBytes(userIdentifier);
		byte[] randomData = null;

		int actualDataSize = USERID_LENGTH_SIZE + TIMESTAMP_SIZE + userIdBytes.length;
		if( actualDataSize < minSecretDataLength){
			randomData = partsEncoder.getRandomBytes(minSecretDataLength - actualDataSize);
		}

		int totalDataLength = (randomData==null) ? actualDataSize : actualDataSize + randomData.length;

		ByteBuffer secretDataBuffer = ByteBuffer.allocate(totalDataLength);
		secretDataBuffer.put(userIdLengthBytes);
		secretDataBuffer.put(tsBytes);
		secretDataBuffer.put(userIdBytes);

		if(randomData!=null) secretDataBuffer.put(randomData);

		return secretDataBuffer.array();
	}
}
