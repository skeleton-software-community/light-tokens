package free.lightokens.api.model.entity;

import org.joda.time.LocalDate;

/**
 * 
 * @author Mounir Regragui
 *
 */
public interface EncryptionKey{

	
	String getPhrase();
	void setPhrase(String phrase);
	LocalDate getDay();
	void setDay(LocalDate day);
	String getAlgorithm();
	void setAlgorithm(String algorithm);

	
	
	
}
