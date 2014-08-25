package free.lighttokens.core.model;

import org.joda.time.LocalDate;

import free.lightokens.api.model.entity.InvalidLToken;

public class InvalidLTokenImpl implements InvalidLToken {

	private LocalDate day;
	private String hash;
	
	public InvalidLTokenImpl() {
	}

	public InvalidLTokenImpl(LocalDate day, String hash) {
		super();
		this.day = day;
		this.hash = hash;
	}

	@Override
	public LocalDate getDay() {
		return day;
	}

	@Override
	public void setDay(LocalDate day) {
		this.day = day;
	}

	@Override
	public String getHash() {
		return hash;
	}

	@Override
	public void setHash(String hash) {
		this.hash = hash;
	}


}
