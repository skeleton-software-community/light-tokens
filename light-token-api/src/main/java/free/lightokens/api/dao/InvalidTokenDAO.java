package free.lightokens.api.dao;

import java.util.Collection;

import org.joda.time.LocalDate;

import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.model.entity.InvalidLToken;

/**
 * Data access object for {@link InvalidLToken}
 * @author Mounir Regragui
 *
 * @param <I> the type of id for {@link InvalidLToken}
 */
public interface InvalidTokenDAO{

	/**
	 * Tests existence of a hash in the invalid token database
	 * @param hash
	 * @return true if the hash exists in the invalid token database
	 * @throws DAOOperationException if an error occurs while accessing the data
	 */
	boolean existsByHash(String hash) throws DAOOperationException;
	
	/**
	 * Inserts an invalid token in the database
	 * @param invalidToken
	 * @throws DAOOperationException if an errors occurs while inserting the data
	 */
	void insertInvalidToken(InvalidLToken invalidToken) throws DAOOperationException;
	
	/**
	 * Deletes an invalid token from the database
	 * @param invalidToken
	 * @throws DAOOperationException
	 */
	void deleteInvalidToken(InvalidLToken invalidToken) throws DAOOperationException;
	
	
	Collection<InvalidLToken> findAllBefore(LocalDate before) throws DAOOperationException;
	
}
