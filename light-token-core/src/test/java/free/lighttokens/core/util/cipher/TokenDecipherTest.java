package free.lighttokens.core.util.cipher;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import free.lightokens.api.exception.KeyGenerationException;
import free.lightokens.api.exception.TokenDecipherException;
import free.lightokens.api.exception.TokenGenerationException;
import free.lightokens.api.model.LTokenInfo;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.model.EncryptionKeyImpl;
import free.lighttokens.core.model.LTokenData;
import free.lighttokens.core.util.genertor.PhraseGenerator;
import free.lighttokens.core.util.secret.SecretDataDecoder;
import free.lighttokens.core.util.secret.SecretDataEncoder;
import free.lighttokens.core.util.secret.SecretPartsEncoder;

public class TokenDecipherTest {

	private static final String ALGO = "AES";
	private static final String VERSION = "test";
	
	private TokenDecipher testee;
	private EncryptionKey key;
	private LocalDate day;
	
	public TokenDecipherTest() throws KeyGenerationException {
		SecretDataDecoder sdEncoder = new SecretDataDecoder();
		testee = new TokenDecipher(sdEncoder, VERSION);
		
		day = new LocalDate();
		String phrase = new PhraseGenerator().generatePhrase(ALGO);
		key = new EncryptionKeyImpl(phrase, day , ALGO);
	}
	
	@Test
	public final void testDecipher() throws TokenDecipherException {
		LocalDateTime timeStamp = day.toLocalDateTime(new LocalTime(23, 30));
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		LTokenData data = cipherTokenData(tokenInfo, timeStamp);
		
		LTokenInfo result = testee.decipher(data, key);
		
		assertEquals(tokenInfo.getUserIdentifier(), result.getUserIdentifier());
	}

	@Test(expected=TokenDecipherException.class)
	public final void testDecipherBadCiphered() throws TokenDecipherException {
		LocalDateTime timeStamp = day.toLocalDateTime(new LocalTime(23, 30));
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		LTokenData data = cipherTokenData(tokenInfo, timeStamp.plusMinutes(1));
		data.setCipheredData("hhhhhhh");
		
		testee.decipher(data, key);
	}
	
	@Test(expected=TokenDecipherException.class)
	public final void testDecipherBadTimestamp() throws TokenDecipherException {
		LocalDateTime timeStamp = day.toLocalDateTime(new LocalTime(23, 30));
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		LTokenData data = cipherTokenData(tokenInfo, timeStamp.plusMinutes(1));
		data.setTimeStamp(new LocalDateTime());
		
		testee.decipher(data, key);

	}
	
	@Test(expected=TokenDecipherException.class)
	public final void testDecipherBadVersion() throws TokenDecipherException {
		LocalDateTime timeStamp = day.toLocalDateTime(new LocalTime(23, 30));
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		LTokenData data = cipherTokenData(tokenInfo, timeStamp.plusMinutes(1));
		data.setVersion("hhhhhhh");
		
		testee.decipher(data, key);
	}

	
	private LTokenData cipherTokenData(LTokenInfo tokenInfo, LocalDateTime timeStamp){
		SecretPartsEncoder partsEncoder = new SecretPartsEncoder();
		SecretDataEncoder sdEncoder = new SecretDataEncoder(partsEncoder, 0);
		TokenCipher cipher = new TokenCipher(sdEncoder, VERSION);
		try {
			return cipher.cipher(tokenInfo, key, timeStamp);
		} catch (TokenGenerationException e) {
			throw new RuntimeException("cipher does not work");
		}
		
	}
}
