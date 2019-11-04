package response;

public class RegisterResponse extends Response {
	private String authToken;
	private String userName;
	private String personID;

	public RegisterResponse(String authToken, String userName, String personID) {
		this.authToken = authToken;
		this.userName = userName;
		this.personID = personID;
	}

	/**
	 * Primarily used for error responses
	 * @param message
	 * @param success
	 */
	public RegisterResponse(String message, Boolean success) {
		super(message, success);
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
