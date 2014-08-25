package free.lighttokens.core.util.cipher;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import free.lightokens.api.exception.KeyGenerationException;
import free.lightokens.api.exception.TokenGenerationException;
import free.lightokens.api.model.LTokenInfo;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.ImplementationVersion;
import free.lighttokens.core.model.EncryptionKeyImpl;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.core.util.genertor.PhraseGenerator;
import free.lighttokens.core.util.secret.SecretDataEncoder;
import free.lighttokens.core.util.secret.SecretPartsEncoder;

public class TokenCipherTest {

	private static final String ALGO = "AES";
	private static double CIPHERFACTOR_MIN = 1.33;
	private EncryptionKey key;
	private LocalDate day;
	
	public TokenCipherTest() throws KeyGenerationException {
		day = new LocalDate();
		String phrase = new PhraseGenerator().generatePhrase(ALGO);
		key = new EncryptionKeyImpl(phrase, day , ALGO);
	}
	
	private TokenCipher buildTestee(int minSecretDataLength) {
		SecretPartsEncoder partsEncoder = new SecretPartsEncoder();
		SecretDataEncoder sdEncoder = new SecretDataEncoder(partsEncoder, minSecretDataLength);
		return new TokenCipher(sdEncoder, ImplementationVersion.IMPL_VERSION);
	}

	@Test
	public final void testCipherWhitoutRandom() throws TokenGenerationException {
		int minSecretDataLength = 0;
		TokenCipher testee = buildTestee(minSecretDataLength);
		LTokenInfo tokenInfo = buildTokenInfo("Kant");
		LocalTime time = new LocalTime(10, 30);
		LocalDateTime timeStamp = day.toLocalDateTime(time);
		
		
		LTokenData result = testee.cipher(tokenInfo, key, timeStamp);
		
		int cipheredLength = result.getCipheredData().length();
		
		assertTrue(cipheredLength>CIPHERFACTOR_MIN*minSecretDataLength);
	}
	
	@Test
	public final void testCipherWhitRandom() throws TokenGenerationException {
		int minSecretDataLength = 5000;
		TokenCipher testee = buildTestee(minSecretDataLength);
		LTokenInfo tokenInfo = buildTokenInfo("Kant");
		LocalTime time = new LocalTime(10, 30);
		LocalDateTime timeStamp = day.toLocalDateTime(time);
		
		
		LTokenData result = testee.cipher(tokenInfo, key, timeStamp);
		
		int cipheredLength = result.getCipheredData().length();
		
		assertTrue(cipheredLength>CIPHERFACTOR_MIN*minSecretDataLength);
	}
	
	@Test(expected=TokenGenerationException.class)
	public final void testCipherBadKeyPhrase() throws TokenGenerationException {
		int minSecretDataLength = 5000;
		TokenCipher testee = buildTestee(minSecretDataLength);
		LTokenInfo tokenInfo = buildTokenInfo("Kant");
		LocalTime time = new LocalTime(10, 30);
		LocalDateTime timeStamp = day.toLocalDateTime(time);
		
		
		EncryptionKey badKey = new EncryptionKeyImpl("ThisIsNotAKey", day , ALGO);
		testee.cipher(tokenInfo, badKey, timeStamp);
	}

	@Test(expected=TokenGenerationException.class)
	public final void testCipherBadKeyAlgo() throws TokenGenerationException, KeyGenerationException {
		int minSecretDataLength = 5000;
		TokenCipher testee = buildTestee(minSecretDataLength);
		LTokenInfo tokenInfo = buildTokenInfo("Kant");
		LocalTime time = new LocalTime(10, 30);
		LocalDateTime timeStamp = day.toLocalDateTime(time);
		
		String goodPhrase = new PhraseGenerator().generatePhrase(ALGO);
		EncryptionKey badKey = new EncryptionKeyImpl(goodPhrase, day , "ThisIsNotAnAlgo");
		testee.cipher(tokenInfo, badKey, timeStamp);
	}

	private LTokenInfo buildTokenInfo(String userIdentifier) {
		LTokenInfo info = new LTokenInfo();
		info.setUserIdentifier(userIdentifier);
		return info;
	}
	
//	public static void main(String[] args) throws TokenGenerationException, KeyGenerationException {
//		TokenCipherTest test = new TokenCipherTest();
//		
//		int numberOfSteps = 100;
//		
//		int[][] results = new int[numberOfSteps][2];
//		
//		int step = 10000;
//		
//		for(int i = 0; i<numberOfSteps; i++){
//			int minDataLength = i*step;
//			int cipheredLength = test.getCipheredLengthCipher(minDataLength);
//			results[i] = new int[]{minDataLength, cipheredLength};
//		}
//		
//		for(int i=0; i<numberOfSteps;i++){
//			double mdl = results[i][0];
//			double cl = results[i][1];
//			double delta = cl/mdl;
//			
//			System.out.println("" + mdl + ':' + cl + " => " + delta );
//		}
//	}//1.33
//	private int getCipheredLengthCipher(int minDatalength) throws TokenGenerationException {
//		TokenCipher testee = buildTestee(minDatalength);
//		LTokenInfo tokenInfo = buildTokenInfo("Kant");
//		LocalTime time = new LocalTime(10, 30);
//		LocalDateTime timeStamp = day.toLocalDateTime(time);
//		
//		
//		LTokenData result = testee.cipher(tokenInfo, key, timeStamp);
//		return result.getCipheredData().length();
//	}

}
