package free.lightokens.api.dao;

import java.util.Collection;

import org.joda.time.LocalDate;

import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.model.entity.EncryptionKey;

/**
 * Data access object for {@link EncryptionKey}
 * 
 * 
 * @author Mounir Regragui
 */
public interface EncryptionKeyDAO{

	/**
	 * Finds the key for a day
	 * @param day
	 * @return the key for that day or null if no key was found for that day
	 * @throws DAOOperationException if an error occurs while accessing the data
	 */
	EncryptionKey getKeyForDayOrNull(LocalDate day) throws DAOOperationException;
	
	/**
	 * Inserts a key in the database
	 * @param key
	 * @throws DAOOperationException if an error occurs while inserting the new data
	 */
	void insertKey(EncryptionKey key) throws DAOOperationException;
	
	/**
	 * Deletes the key for a particular day in the database
	 * @param day
	 * @throws DAOOperationException if an error occurs while deleting data
	 */
	void deleteKeyForDay(LocalDate day)  throws DAOOperationException;
	
	/**
	 * Finds all the keys before a particular day. The day limit is exclusive,
	 * meaning that no keys should match the day passed as an argument
	 * @param before the day limit (exclusive)
	 * @return all the key associated to a day before (exclusively) the limit
	 * @throws DAOOperationException if an error occurs while accessing the data
	 */
	Collection<EncryptionKey> findAllBefore(LocalDate before)  throws DAOOperationException;
	
}
