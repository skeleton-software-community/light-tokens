package free.lightokens.api.model.entity;

import org.joda.time.LocalDate;

/**
 * 
 * @author Mounir Regragui
 *
 */
public interface InvalidLToken{

	LocalDate getDay();
	void setDay(LocalDate day);
	String getHash();
	void setHash(String hash);
}
