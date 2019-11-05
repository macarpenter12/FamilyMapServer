package DAO;

import exception.DataAccessException;
import familymap.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * Handles database access operations for Person objects.
 */
public class PersonDAO {
	private Connection conn;

	/**
	 * @param conn The Connection object to link to this DAO.
	 */
	public PersonDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param person The Person object to be inserted into the database.
	 * @throws DataAccessException If error occurred when accessing data in the database.
	 */
	public void insert(Person person) throws DataAccessException {
		String sql = "INSERT INTO person_table " +
				"(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID) " +
				"VALUES(?,?,?,?,?,?,?,?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, person.getPersonID());
			stmt.setString(2, person.getAssociatedUsername());
			stmt.setString(3, person.getFirstName());
			stmt.setString(4, person.getLastName());
			stmt.setString(5, person.getGender());
			stmt.setString(6, person.getFatherID());
			stmt.setString(7, person.getMotherID());
			stmt.setString(8, person.getSpouseID());

			stmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DataAccessException(ex.getMessage());
		}
	}

	/**
	 * @param personID The personID related to the Person to search the database for.
	 * @return The Person object that has personID, if found. Otherwise, null.
	 * @throws DataAccessException If error occurred when accessing data.
	 */
	public Person find(String personID) throws DataAccessException {
		Person person;
		ResultSet rs = null;
		String sql = "SELECT * FROM person_table WHERE personID = ?;";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, personID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				person = new Person(rs.getString("personID"),
						rs.getString("associatedUsername"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("gender"),
						rs.getString("fatherID"),
						rs.getString("motherID"),
						rs.getString("spouseID")
				);
				return person;
			}
		} catch (SQLException ex) {
			throw new DataAccessException("Error occurred while finding person " + personID);
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
	 * Finds all Person objects associated with the given username.
	 *
	 * @param userName The username to search for.
	 * @return A list of Person objects associated with the given username.
	 * @throws DataAccessException If error occurred when accessing data.
	 */
	public ArrayList<Person> findByUser(String userName) throws DataAccessException {
		ArrayList<Person> persons = new ArrayList<>();
		ResultSet rs = null;
		String sql = "SELECT * FROM person_table WHERE associatedUsername = ?;";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			rs = stmt.executeQuery();
			while (rs.next()) {
				persons.add(new Person(rs.getString("personID"),
						rs.getString("associatedUsername"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("gender"),
						rs.getString("fatherID"),
						rs.getString("motherID"),
						rs.getString("spouseID")
				));
			}
			return persons;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw new DataAccessException("Error occurred while finding persons with associated username " + userName);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Removes all Person objects with associated Username specified from database.
	 *
	 * @param userName The username to search for and remove.
	 * @throws DataAccessException If error occurred when accessing data.
	 */
	public void deleteByUser(String userName) throws DataAccessException {
		String sql = "DELETE FROM person_table WHERE associatedUsername = ?;";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DataAccessException("SQL Error encountered while deleting all persons " +
					"with associatedUsername " + userName + " from table");
		}
	}

	/**
	 * Clears all items from the person table, but does not delete the table itself.
	 *
	 * @throws DataAccessException If error occurred while accessing data.
	 */
	public void clearTable() throws DataAccessException {
		try (Statement stmt = conn.createStatement()) {
			String sql = "DELETE FROM person_table";
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			throw new DataAccessException("SQL Error encountered while clearing person table");
		}
	}
}
