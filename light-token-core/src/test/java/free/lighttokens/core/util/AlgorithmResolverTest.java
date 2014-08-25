package free.lighttokens.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Works with the SunJCE provider
 * @author Mounir Regragui
 * 
 * @see http://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
 *
 */
public class AlgorithmResolverTest {

	
	
	@Test
	public final void testResolveCipherAlgorithm() {
		AlgorithmResolver testee = new AlgorithmResolver("Blowfish");
		String result = testee.resolveCipherAlgorithm();
		assertEquals("Blowfish", result);
	}

	@Test
	public final void testResolveUnknownCipherAlgorithm() {
		AlgorithmResolver testee = new AlgorithmResolver("hhhhhhh");
		String result = testee.resolveCipherAlgorithm();
		assertEquals("AES", result);
	}
	
	@Test
	public final void testResolveNullCipherAlgorithm() {
		AlgorithmResolver testee = new AlgorithmResolver(null);
		String result = testee.resolveCipherAlgorithm();
		assertEquals("AES", result);
	}

}
