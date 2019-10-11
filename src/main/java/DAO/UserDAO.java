package DAO;

import exception.DataAccessException;
import familymap.User;

import java.sql.Connection;

/**
 * Handles database access operations for User objects and the user table.
 */
public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn) { this.conn = conn; }

    public boolean insert(User user) throws DataAccessException { return false; }

    public User find(User user) { return null; }
}
