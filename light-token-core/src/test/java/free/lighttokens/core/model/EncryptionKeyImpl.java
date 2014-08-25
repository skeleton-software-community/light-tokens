package free.lighttokens.core.model;

import org.joda.time.LocalDate;

import free.lightokens.api.model.entity.EncryptionKey;

public class EncryptionKeyImpl implements EncryptionKey{
	
	private String phrase;
	private LocalDate day;
	private String algorithm;
	public String getPhrase() {
		return phrase;
	}
	
	public EncryptionKeyImpl() {
	}
	
	public EncryptionKeyImpl(String phrase, LocalDate day, String algorithm) {
		super();
		this.phrase = phrase;
		this.day = day;
		this.algorithm = algorithm;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public LocalDate getDay() {
		return day;
	}
	public void setDay(LocalDate day) {
		this.day = day;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	

}
