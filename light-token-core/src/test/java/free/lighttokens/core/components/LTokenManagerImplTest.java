package free.lighttokens.core.components;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import free.lightokens.api.components.BatchPerformer;
import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.exception.EncryptionKeyOperationException;
import free.lightokens.api.exception.TokenAuthenticationException;
import free.lightokens.api.exception.TokenDecipherException;
import free.lightokens.api.exception.TokenGenerationException;
import free.lightokens.api.model.LTokenConfiguration;
import free.lightokens.api.model.LTokenInfo;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.ImplementationVersion;
import free.lighttokens.core.factory.BatchPerformerFactory;
import free.lighttokens.core.factory.DefaultConfigurationBuilder;
import free.lighttokens.core.factory.LTokenManagerFactory;
import free.lighttokens.core.model.EncryptionKeyImpl;
import free.lighttokens.core.util.TimestampConvertorUtil;
import free.lighttokens.exceptions.BadTokenVersionException;
import free.lighttokens.exceptions.MalformedTokenException;
import free.lighttokens.mock.EncryptionKeyDAOMock;
import free.lighttokens.mock.EncryptionKeyEntityBuilder;
import free.lighttokens.mock.InvalidTokenDAOMock;
import free.lighttokens.mock.InvalidTokenEntityBuilder;

public class LTokenManagerImplTest {

	private static final int TOKEN_VALIDITY = 4;
	private static final int FUTURE_KEYS = 2;
	private static final int TOKEN_REGEN = 2;
	private static final LocalDate TODAY = new LocalDate();
	private LTokenManagerImpl testee;
	
	public LTokenManagerImplTest() throws EncryptionKeyOperationException, DAOOperationException {
		EncryptionKeyDAO keyDao = new EncryptionKeyDAOMock();
		InvalidTokenDAO invalidDao = new InvalidTokenDAOMock();
		LTokenConfiguration config = DefaultConfigurationBuilder.buildWithDefaults(TOKEN_VALIDITY, FUTURE_KEYS, TOKEN_REGEN);
		initDb(keyDao, invalidDao, config);
		testee = (LTokenManagerImpl) LTokenManagerFactory.build(keyDao, invalidDao, new InvalidTokenEntityBuilder(), config);
	}
	
	private void initDb(EncryptionKeyDAO keyDao, InvalidTokenDAO invalidDao, LTokenConfiguration config) throws EncryptionKeyOperationException, DAOOperationException {
		BatchPerformer bp = BatchPerformerFactory.build(keyDao, invalidDao, new EncryptionKeyEntityBuilder(), config);
		bp.generateKeys();
		EncryptionKey key = keyDao.getKeyForDayOrNull(TODAY.plusDays(1));
		EncryptionKey key2day = new EncryptionKeyImpl(key.getPhrase(), TODAY, key.getAlgorithm());
		keyDao.insertKey(key2day);
	}

	@Test
	public final void testGenerateToken() throws TokenGenerationException {
		LTokenInfo tokenInfo = new LTokenInfo("Kant");
		
		String result = testee.generateToken(tokenInfo);
		assertValidToken(result);
		
	}

	private void assertValidToken(String result) {
		assertTrue(3<=result.split(";").length);
	}

	@Test
	public final void testAuthenticate() throws TokenGenerationException, TokenAuthenticationException, TokenDecipherException {
		LTokenInfo in = new LTokenInfo("Hegel");
		String token = testee.generateToken(in);
		
		LTokenInfo result = testee.authenticate(token);
		assertEquals("Hegel", result.getUserIdentifier());
	}

	@Test
	public final void testNeedsNewToken() throws MalformedTokenException, BadTokenVersionException {
		LocalDateTime regenDay = TODAY.minusDays(TOKEN_REGEN).toLocalDateTime(new LocalTime());
		String newToken = ImplementationVersion.IMPL_VERSION + ';' + TimestampConvertorUtil.toString(regenDay.plusHours(1))
				+ ';' + "hhhhhhh";
		
		String oldToken = ImplementationVersion.IMPL_VERSION + ';' + TimestampConvertorUtil.toString(regenDay.minusHours(1))
				+ ';' + "hhhhhhh";
		
		
		assertFalse(testee.needsNewToken(newToken));
		assertTrue(testee.needsNewToken(oldToken));
	}

	@Test(expected=TokenAuthenticationException.class)
	public final void testInvalidateToken() throws TokenAuthenticationException, TokenDecipherException{
		LTokenInfo invalid = new LTokenInfo("Leibniz");
		String token = null;
		try {
			token = testee.generateToken(invalid);
			testee.invalidateToken(token);
		} catch (TokenGenerationException | MalformedTokenException | BadTokenVersionException |
				TokenAuthenticationException | DAOOperationException e) {
			fail(e.getMessage());
		}
		
		testee.authenticate(token);
	}

}
