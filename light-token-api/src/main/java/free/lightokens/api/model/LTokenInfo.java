package free.lightokens.api.model;

public class LTokenInfo {

	private String userIdentifier;
	
	public LTokenInfo() {}
	
	public LTokenInfo(String userIdentifier) {
		super();
		this.userIdentifier = userIdentifier;
	}



	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	
	
}
