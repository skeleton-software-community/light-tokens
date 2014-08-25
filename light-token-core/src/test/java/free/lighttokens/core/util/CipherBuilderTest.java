package free.lighttokens.core.util;

import static org.junit.Assert.*;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;

import org.joda.time.LocalDate;
import org.junit.Test;

import free.lightokens.api.exception.KeyGenerationException;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.model.EncryptionKeyImpl;
import free.lighttokens.core.util.genertor.PhraseGenerator;

public class CipherBuilderTest {

	private static final String ALGO = "AES";
	private PhraseGenerator phraseGen = new PhraseGenerator();
	
	@Test
	public final void testCreateCipher() throws GeneralSecurityException {
		String phrase = generatePhrase();
		EncryptionKey key = new EncryptionKeyImpl(phrase, new LocalDate(), ALGO);
		Cipher result = CipherBuilder.createCipher(key, Cipher.ENCRYPT_MODE);
		
		assertEquals(ALGO, result.getAlgorithm());
	}
	
	@Test(expected=GeneralSecurityException.class)
	public final void testCreateWithUnknownAlgo() throws GeneralSecurityException {
		EncryptionKey key = new EncryptionKeyImpl("ThisIsNotAKey", new LocalDate(), "ThisIsNotAnAlgo");
		CipherBuilder.createCipher(key, Cipher.ENCRYPT_MODE);
	}

	private String generatePhrase(){
		String phrase;
		try {
			phrase = phraseGen.generatePhrase(ALGO);
		} catch (KeyGenerationException e) {
			throw new RuntimeException("Test set up failed");
		}
		return phrase;
	}

}
