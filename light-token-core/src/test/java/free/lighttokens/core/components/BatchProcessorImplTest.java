package free.lighttokens.core.components;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Test;

import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.exception.EncryptionKeyOperationException;
import free.lightokens.api.model.LTokenConfiguration;
import free.lightokens.api.model.entity.InvalidLToken;
import free.lighttokens.core.factory.DefaultConfigurationBuilder;
import free.lighttokens.core.factory.BatchPerformerFactory;
import free.lighttokens.core.model.EncryptionKeyImpl;
import free.lighttokens.core.model.InvalidLTokenImpl;
import free.lighttokens.mock.EncryptionKeyDAOMock;
import free.lighttokens.mock.EncryptionKeyEntityBuilder;
import free.lighttokens.mock.InvalidTokenDAOMock;

public class BatchProcessorImplTest {
	
	private static final int TOKEN_VALIDITY = 4;
	private static final int FUTURE_KEYS = 2;
	private static final int TOKEN_REGEN = 2;
	private static final LocalDate TODAY = new LocalDate();
	private LTokenConfiguration config;
	
	public BatchProcessorImplTest() throws DAOOperationException, EncryptionKeyOperationException {
		config = DefaultConfigurationBuilder.buildWithDefaults(TOKEN_VALIDITY, FUTURE_KEYS, TOKEN_REGEN);
	}

	
	private BatchPerformerImpl generateTestee(EncryptionKeyDAO keyDao, InvalidTokenDAO invalidDao){
		return (BatchPerformerImpl) BatchPerformerFactory.build(keyDao, invalidDao, new EncryptionKeyEntityBuilder(), config);
		
	}

	@Test
	public final void testCleanOldKeys() throws EncryptionKeyOperationException, DAOOperationException {
		EncryptionKeyDAOMock keyDao = new EncryptionKeyDAOMock();
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(5), "AES"));
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(4), "AES"));
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(3), "AES"));
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(2), "AES"));
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(1), "AES"));
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(0), "AES"));
		BatchPerformerImpl testee = generateTestee(keyDao, null);
		testee.generateKeys();
		
		testee.cleanOldKeys();
		
		for(int i=0; i<TOKEN_VALIDITY; i++){
			assertNotNull(keyDao.getKeyForDayOrNull(TODAY.minusDays(i)));
		}
		for(int i=1; i<FUTURE_KEYS+1; i++){
			assertNotNull(keyDao.getKeyForDayOrNull(TODAY.plusDays(i)));
		}
		assertNull(keyDao.getKeyForDayOrNull(TODAY.minusDays(TOKEN_VALIDITY+1)));
		
	}
	
	@Test
	public final void testGenerateKeys() throws EncryptionKeyOperationException, DAOOperationException {
		EncryptionKeyDAOMock keyDao = new EncryptionKeyDAOMock();
		keyDao.insertKey(new EncryptionKeyImpl("key1", TODAY.minusDays(0), "AES"));
		
		BatchPerformerImpl testee = generateTestee(keyDao, null);
		
		testee.generateKeys();
		for(int i=0; i<FUTURE_KEYS+1; i++){
			assertNotNull(keyDao.getKeyForDayOrNull(TODAY.plusDays(i)));
		}
		assertNull(keyDao.getKeyForDayOrNull(TODAY.plusDays(FUTURE_KEYS+1)));
	}

	@Test
	public final void testCleanOldInvalidTokens() throws EncryptionKeyOperationException, DAOOperationException {
		InvalidTokenDAO invalidDao = new InvalidTokenDAOMock();
		insertInvalid(invalidDao, "today", TODAY);
		insertInvalid(invalidDao, "close", TODAY.minusDays(TOKEN_VALIDITY));
		insertInvalid(invalidDao, "oldenough", TODAY.minusDays(TOKEN_VALIDITY+1));
		insertInvalid(invalidDao, "old", TODAY.minusDays(TOKEN_VALIDITY+10));
		BatchPerformerImpl testee = generateTestee(null, invalidDao);
		
		testee.cleanOldInvalidTokens();
		
		assertTrue(invalidDao.existsByHash("today"));
		assertTrue(invalidDao.existsByHash("close"));
		assertFalse(invalidDao.existsByHash("oldenough"));
		assertFalse(invalidDao.existsByHash("old"));
	}


	private InvalidLToken insertInvalid(InvalidTokenDAO invalidDao, String hash, LocalDate day) {
		InvalidLToken result = new InvalidLTokenImpl();
		result.setDay(day);
		result.setHash(hash);
		
		try {
			invalidDao.insertInvalidToken(result);
		} catch (DAOOperationException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

}
