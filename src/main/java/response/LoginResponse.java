package response;

public class LoginResponse extends Response {
	private String authToken;
	private String userName;
	private String personID;

	// For error or failure responses
	public LoginResponse(String message, Boolean success) {
		super(message, success);
	}

	public LoginResponse(String authToken, String userName, String personID) {
		this.authToken = authToken;
		this.userName = userName;
		this.personID = personID;
	}

	public LoginResponse(String authToken, String userName, String personID, String message, Boolean success) {
		super(message, success);
		this.authToken = authToken;
		this.userName = userName;
		this.personID = personID;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}
}
