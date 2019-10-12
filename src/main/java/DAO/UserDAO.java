package DAO;

import exception.DataAccessException;
import familymap.User;

import java.sql.Connection;

/**
 * Handles database access operations for User objects and the user table.
 */
public class UserDAO {
    private Connection conn;

    /**
     * @param conn The Connection object to link to this DAO.
     */
    public UserDAO(Connection conn) { this.conn = conn; }

    /**
     * @param user The User object to be added to the database user table.
     * @return Whether the User was added or not.
     * @throws DataAccessException If error occurred when adding the User.
     */
    public boolean insert(User user) throws DataAccessException { return false; }

    /**
     * @param username The username associated with the User object to search for.
     * @return If the User object is found, return that User object. Otherwise, null.
     * @throws DataAccessException If error occurred when adding the User.
     */
    public User find(String username) throws DataAccessException { return null; }
}
