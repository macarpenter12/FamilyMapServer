package service;

import DAO.AuthTokenDAO;
import DAO.Database;
import DAO.UserDAO;
import exception.DataAccessException;
import familymap.AuthToken;
import familymap.User;
import request.LoginRequest;
import response.LoginResponse;

/**
 * Take credentials and return an authentication token. Server can call a function which
 * will call this class. Will help Gson return json data.
 */
public class LoginService {

    /**
     * @param loginReq The Object containing the info for the login request
     * @return Returns the response as an Object easily converted to JSON
     */
    public LoginResponse login(LoginRequest loginReq) throws DataAccessException {
        String userName = loginReq.getUserName();
        String password = loginReq.getPassword();
        User user = null;
        AuthToken authToken = null;

        Database db = new Database();
        try {
            UserDAO uDao = new UserDAO(db.openConnection());

            // Find the user with the given username and match the password
            user = uDao.find(userName);
            db.closeConnection(true);

            if (user != null) {
                if (password.equals(user.getPassword())) {
                    // Generate an AuthToken
                    authToken = new AuthToken(userName);
                    // Insert AuthToken into database
                    AuthTokenDAO atDAO = new AuthTokenDAO(db.getConnection());
                    atDAO.insert(authToken);
                    db.closeConnection(true);
                }
                else {
                    return new LoginResponse("Incorrect password. Please try again.", false);
                }
            }
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            throw ex;
        }
        if (user == null) {
            // error
            return new LoginResponse("Unable to find user " + userName, false);
        }
        else {
            return new LoginResponse(authToken.getAuthToken(), user.getUserName(), user.getPersonID());
        }
    }
}
