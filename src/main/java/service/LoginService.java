package service;

/**
 * Take credentials and return an authentication token. Server can call a function which
 * will call this class. Will help Gson return json data.
 */
public class LoginService {
    private String authToken;
    private String userName;
    private String personID;

    /**
     * @param request The json object to process.
     * @return Returns the response as a json object.
     */
    public String login(String request) {
        return null;
    }
}
