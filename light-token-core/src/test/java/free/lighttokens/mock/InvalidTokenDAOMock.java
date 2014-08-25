package free.lighttokens.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.model.entity.InvalidLToken;

public class InvalidTokenDAOMock implements InvalidTokenDAO{

	private Map<String, InvalidLToken> db = new HashMap<>();
	
	@Override
	public boolean existsByHash(String hash) throws DAOOperationException {
		return db.containsKey(hash);
	}

	@Override
	public void insertInvalidToken(InvalidLToken invalidToken) throws DAOOperationException {
		if(existsByHash(invalidToken.getHash())) throw new DAOOperationException("Hash already exists");
		db.put(invalidToken.getHash(), invalidToken);
	}

	@Override
	public void deleteInvalidToken(InvalidLToken invalidToken) throws DAOOperationException {
		if(!existsByHash(invalidToken.getHash())) throw new DAOOperationException("Hash does not exist");
		db.remove(invalidToken.getHash());
	}

	@Override
	public Collection<InvalidLToken> findAllBefore(LocalDate before) throws DAOOperationException {
		Set<InvalidLToken> result = new HashSet<>();
		for(InvalidLToken k : db.values()){
			if(k.getDay().isBefore(before)) result.add(k);
		}
		return result;
	}

}
