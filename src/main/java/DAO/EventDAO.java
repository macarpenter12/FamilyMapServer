package DAO;

import exception.DataAccessException;
import familymap.Event;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Handles database access operations for Event objects and the event table.
 */
public class EventDAO {
    private Connection conn;

    /**
     * @param conn The Connection object to link to this DAO.
     */
    public EventDAO(Connection conn) { this.conn = conn; }

    /**
     * @param event The Event object you want to insert into the table.
     * @return Whether the Event object could be added to the table or not.
     * @throws DataAccessException Custom Exception used so that not all DAO classes have to be JDBC. Replaces SQL Exception.
     */
    public boolean insert(Event event) throws DataAccessException { return false; }

    /**
     * @param eventID eventID member of Event object to be found in the table.
     * @return Event object that has member object eventID.
     * @throws DataAccessException Custom Exception used so that not all DAO classes have to be JDBC. Replaces SQL Exception.
     */
    public Event find(String eventID) throws DataAccessException { return null; }

    /**
     * Finds all Events associated with an input username.
     * @param username The username to search for.
     * @return A list of all Events associated with the given username.
     * @throws DataAccessException If error occurred when accessing data.
     */
    public ArrayList<Event> findByUser(String username) throws DataAccessException { return null; }

    /**
     * Finds all Events associated with the given username, then removes them from existing tables.
     * @param username The username to search for.
     * @throws DataAccessException If error occurred when accessing data.
     */
    public void deleteByUser(String username) throws DataAccessException { }
}
