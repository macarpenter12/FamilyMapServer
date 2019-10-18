package DAO;

import exception.DataAccessException;
import familymap.Event;

import java.sql.*;
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
     * @throws DataAccessException Custom Exception used so that not all DAO classes have to be JDBC. Replaces SQL Exception.
     */
    public void insert(Event event) throws DataAccessException {
        // Initialize sql statement
        String sql = "INSERT INTO event_table " +
                "(eventID, username, personID, latitude, longitude, country, city, type, year)" +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        // Update sql using prepared statement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting event into the database");
        }
    }

    /**
     * @param eventID eventID member of Event object to be found in the table.
     * @return Event object that has member object eventID.
     * @throws DataAccessException Custom Exception used so that not all DAO classes have to be JDBC. Replaces SQL Exception.
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event_table WHERE eventID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"),
                        rs.getString("username"),
                        rs.getString("personID"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("type"),
                        rs.getInt("year")
                        );
                return event;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error occurred while finding event " + eventID);
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
     * Clears all items from the event table, but does not delete the table itself.
     * @throws DataAccessException If error occurred while accessing data.
     */
    public void clearTable() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM event_table";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new DataAccessException("SQL Error encountered while clearing event table");
        }
    }

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
