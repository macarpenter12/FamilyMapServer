package DAO;

import exception.DataAccessException;
import familymap.Person;

import java.sql.Connection;

/**
 * Handles database access operations for Person objects.
 */
public class PersonDAO {
    private Connection conn;

    /**
     * @param conn The Connection object to link to this DAO.
     */
    public PersonDAO(Connection conn) { this.conn = conn; }

    /**
     * @param person The Person object to be inserted into the database.
     * @return Whether the Person obeject was successfully inserted or not.
     * @throws DataAccessException If error occurred when accessing data in the database.
     */
    public boolean insert(Person person) throws DataAccessException { return false; }

    /**
     * @param personID The personID related to the Person to search the database for.
     * @return The Person object that has personID, if found. Otherwise, null.
     * @throws DataAccessException If error occurred when accessing data.
     */
    public Person find(String personID) throws DataAccessException { return null; }
}
