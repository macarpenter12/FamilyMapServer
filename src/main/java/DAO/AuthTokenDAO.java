package DAO;

import server.AuthToken;

import java.sql.Connection;

/**
 * Handles database access operations for Authentication Tokens.
 */
public class AuthTokenDAO {
    private Connection conn;

    /**
     * @param conn The Connection object to link to this DAO.
     */
    public AuthTokenDAO(Connection conn) { this.conn = conn; }

    /**
     * @param authtoken The Authentication Token to be added to the database table.
     * @return True if token was inserted to the table properly.
     */
    public boolean insert(AuthToken authtoken) { return false; }

    /**
     * @param token The data for the Authentication Token to be found.
     * @return The AuthToken if found, null otherwise.
     */
    public AuthToken find(String token) { return null; }
}
