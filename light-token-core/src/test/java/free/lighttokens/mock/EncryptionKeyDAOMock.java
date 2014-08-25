package free.lighttokens.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.exception.DAOOperationException;
import free.lightokens.api.model.entity.EncryptionKey;

public class EncryptionKeyDAOMock implements EncryptionKeyDAO{

	private Map<LocalDate, EncryptionKey> db = new HashMap<>();
	
	@Override
	public EncryptionKey getKeyForDayOrNull(LocalDate day) throws DAOOperationException {
		return db.get(day);
	}

	@Override
	public void insertKey(EncryptionKey key) throws DAOOperationException {
		EncryptionKey existing = getKeyForDayOrNull(key.getDay());
		if(existing!=null){
			throw new DAOOperationException("Key already exists");
		}else{
			db.put(key.getDay(), key);
		}
	}

	@Override
	public void deleteKeyForDay(LocalDate day) throws DAOOperationException {
		EncryptionKey existing = getKeyForDayOrNull(day);
		if(existing==null){
			throw new DAOOperationException("Key does not exist");
		}else{
			db.remove(day);
		}
	}

	@Override
	public Collection<EncryptionKey> findAllBefore(LocalDate before) throws DAOOperationException {
		Set<EncryptionKey> result = new HashSet<>();
		for(EncryptionKey k : db.values()){
			if(k.getDay().isBefore(before)) result.add(k);
		}
		return result;
	}

}
