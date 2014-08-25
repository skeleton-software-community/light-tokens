package free.lighttokens.core.model;

import org.joda.time.LocalDateTime;

public class LTokenData {

	private String cipheredData;
	private LocalDateTime timeStamp;
	private String version;
	
	public LTokenData(String cipheredData, LocalDateTime timeStamp,
			String version) {
		super();
		this.cipheredData = cipheredData;
		this.timeStamp = timeStamp;
		this.version = version;
	}
	public String getCipheredData() {
		return cipheredData;
	}
	public void setCipheredData(String cipheredData) {
		this.cipheredData = cipheredData;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	@Override
	public String toString() {
		return "LTokenData [cipheredData=" + cipheredData + ", timeStamp=" + timeStamp + ", version=" + version + "]";
	}
	
	
	
	
}
