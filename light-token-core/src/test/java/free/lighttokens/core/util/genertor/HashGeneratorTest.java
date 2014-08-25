package free.lighttokens.core.util.genertor;

import static org.junit.Assert.*;

import org.junit.Test;

public class HashGeneratorTest {

	private static final int SHA1_LENGTH = 28;
	private HashGenerator hashGen = new HashGenerator();
	
	
	@Test
	public final void testHash() {
		String token = "hhh";
		String result = hashGen.hash(token);
		assertEquals(SHA1_LENGTH, result.length());
	}

}
