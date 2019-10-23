package DAO;

import exception.DataAccessException;
import server.AuthToken;

import java.sql.*;

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
     */
    public void insert(AuthToken authtoken) throws DataAccessException {
        String sql = "INSERT INTO authtoken_table " +
                "(authtoken, username) " +
                "VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken.getToken());
            stmt.setString(2, authtoken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting AuthToken into database");
        }
    }

    /**
     * @param token The data for the Authentication Token to be found.
     * @return The AuthToken if found, null otherwise.
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authtoken;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtoken_table WHERE token = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authtoken = new AuthToken(rs.getString("token"), rs.getString("username"));
                return authtoken;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while finding user " + token);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void cleartable() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM authtoken_table";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new DataAccessException("SQL Error encountered while clearing AuthToken table");
        }
    }
}
