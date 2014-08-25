package free.lighttokens.core.util.genertor;

import static org.junit.Assert.*;

import org.junit.Test;

import free.lightokens.api.exception.KeyGenerationException;

public class PhraseGeneratorTest {

	private static final String ALGO = "AES";
	private static final int KEY_LENGTH = 24;
	
	private PhraseGenerator testee = new PhraseGenerator();
	
	@Test
	public final void testGeneratePhrase() throws KeyGenerationException {
		String phrase = testee.generatePhrase(ALGO);
		assertEquals(KEY_LENGTH, phrase.length());
	}
	
	@Test(expected=KeyGenerationException.class)
	public final void testUnknownAlgorithm() throws KeyGenerationException {
		testee.generatePhrase("hhhhhhhhhh");
	}
}
