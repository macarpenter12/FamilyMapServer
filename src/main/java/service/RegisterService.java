package service;

import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import exception.DataAccessException;
import familymap.Person;
import familymap.User;
import request.LoginRequest;
import request.RegisterRequest;
import response.FillResponse;
import response.LoginResponse;
import response.RegisterResponse;

import java.util.UUID;

/**
 * Registers a new user. Server can call a function which will call this class. Will also
 * help Gson to return json data.
 */
public class RegisterService {
    private String authToken;
    private String userName;
    private String personID;

    /**
     * @param request The json object to process.
     * @return Returns the response as a json object.
     */
    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        Database db = new Database();

        // Generate new Person data to associate with User. Father/mother/spouse IDs are null for now, but will be
        // generated when we fill the data
        Person person = new Person(UUID.randomUUID().toString(), request.getUserName(), request.getFirstName(),
                request.getLastName(), request.getGender(), null, null, null);
        // Link Person with User
        User user = new User(request.getUserName(), request.getPassword(), request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getGender(), person.getPersonID());
        try {
            // Insert Person and User into database
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.insert(person);
            db.closeConnection(true);

            UserDAO uDao = new UserDAO(db.openConnection());
            uDao.insert(user);
            db.closeConnection(true);

            // Fill 4 generations of data
            FillService fillServ = new FillService();
            FillResponse fillRes = fillServ.fill(user.getUserName(), 4);

            // Log the user in
            LoginRequest loginReq = new LoginRequest(user.getUserName(), user.getPassword());
            LoginService loginServ = new LoginService();
            LoginResponse loginRes = loginServ.login(loginReq);

            // Create response
            return new RegisterResponse(loginRes.getAuthToken(), loginRes.getUserName(),
                    loginRes.getPersonID());

        } catch (DataAccessException ex) {
            db.closeConnection(false);
            throw ex;
        }
    }
}
