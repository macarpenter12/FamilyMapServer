package service;

import com.google.gson.Gson;
import familymap.User;

/**
 * Registers a new user. Server can call a function which will call this class. Will also
 * help Gson to return json data.
 */
public class RegisterUserService {
    private String authToken;
    private String userName;
    private String personID;

    /**
     * @param request The json object to process.
     * @return Returns the response as a json object.
     */
    public String register(String request) throws Exception {
        try {
            Gson gson = new Gson();
            User user = gson.fromJson(request, User.class);
        } catch (Exception ex) {
            throw ex;
        }

        return null;
    }
}
