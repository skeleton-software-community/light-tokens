package free.lightokens.api.components;

import free.lightokens.api.exception.EncryptionKeyOperationException;

/**
 * A component that performs daily batch tasks
 * 
 * Usually, task will be scheduled to run the methods of this component
 * The methods can be called in any order
 * 
 * @author Mounir Regragui
 *
 */
public interface BatchPerformer {

	/**
	 * Deletes all encryption keys that are considered old from the data source
	 * @throws EncryptionKeyOperationException in case of an error during a data source operation
	 */
	void cleanOldKeys() throws EncryptionKeyOperationException;
	
	/**
	 * Generates future encryption keys
	 * @throws EncryptionKeyOperationException in case of an error during a data source operation
	 */
	void generateKeys() throws EncryptionKeyOperationException;
	
	/**
	 * Deletes all invalid token hashs that are considered old from the data source
	 * @throws EncryptionKeyOperationException in case of an error during a data source operation
	 */
	void cleanOldInvalidTokens() throws EncryptionKeyOperationException;
}
