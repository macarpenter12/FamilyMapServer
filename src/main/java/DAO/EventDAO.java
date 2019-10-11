package DAO;

import exception.DataAccessException;
import familymap.Event;

import java.sql.Connection;

/**
 * Handles the database access operations for Event objects and the event table.
 */
public class EventDAO {
    private Connection conn;

    /**
     * @param conn The Connection object that you want to link to this DAO.
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
}
