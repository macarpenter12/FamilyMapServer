package DAO;

import exception.DataAccessException;
import familymap.User;

import java.sql.*;

/**
 * Handles database access operations for User objects and the user table.
 */
public class UserDAO {
	private Connection conn;

	/**
	 * @param conn The Connection object to link to this DAO.
	 */
	public UserDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param user The User object to be added to the database user table.
	 * @return Whether the User was added or not.
	 * @throws DataAccessException If error occurred when adding the User.
	 */
	public void insert(User user) throws DataAccessException {
		String sql = "INSERT INTO user_table " +
				"(userName, password, email, firstName, lastName, gender, personID) " +
				"VALUES(?,?,?,?,?,?,?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getFirstName());
			stmt.setString(5, user.getLastName());
			stmt.setString(6, user.getGender());
			stmt.setString(7, user.getPersonID());

			stmt.executeUpdate();
		} catch (SQLException ex) {
			// CONSTRAINT ERROR
			if (ex.getErrorCode() == 19) {
				String message = ex.getMessage();
				if (message.contains("UNIQUE")) {
					throw new DataAccessException("Username already taken by another user");
				}
				else if (message.contains("NOT NULL")) {
				    throw new DataAccessException("Request property missing or has invalid value");
				}
			}
			// Internal error, unknown
			else {
                throw new DataAccessException("Internal server error");
            }
		}
	}

	/**
	 * @param username The username associated with the User object to search for.
	 * @return If the User object is found, return that User object. Otherwise, null.
	 * @throws DataAccessException If error occurred when adding the User.
	 */
	public User find(String username) throws DataAccessException {
		User user;
		ResultSet rs = null;
		String sql = "SELECT * FROM user_table WHERE userName = ?;";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			if (rs.next()) {
				user = new User(rs.getString("userName"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("gender"),
						rs.getString("personID")
				);
				return user;
			}
		} catch (SQLException ex) {

			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw new DataAccessException("Error occurred while finding user " + username);
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

	/**
	 * Clears all items from user table, but does not delete the table itself.
	 *
	 * @throws DataAccessException If error occurred while accessing data.
	 */
	public void clearTable() throws DataAccessException {
		try (Statement stmt = conn.createStatement()) {
			String sql = "DELETE FROM user_table";
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw new DataAccessException("SQL Error encountered while clearing user table");
		}
	}
}
