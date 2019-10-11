package DAO;

import exception.DataAccessException;
import familymap.Event;

import java.sql.Connection;

public class EventDAO {
    private Connection conn;

    public EventDAO(Connection conn) { this.conn = conn; }

    public boolean insert(Event event) throws DataAccessException { return false; }

    public Event find(String eventID) throws DataAccessException { return null; }
}
