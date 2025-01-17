package DAO;

import exception.DataAccessException;
import familymap.Event;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Handles database access operations for Event objects and the event table.
 */
public class EventDAO {
	private Connection conn;

	/**
	 * @param conn The Connection object to link to this DAO.
	 */
	public EventDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param event The Event object you want to insert into the table.
	 * @throws DataAccessException Custom Exception used so that not all DAO classes have to be JDBC. Replaces SQL Exception.
	 */
	public void insert(Event event) throws DataAccessException {
		// Initialize sql statement
		String sql = "INSERT INTO event_table " +
				"(eventType, personID, city, country, latitude, longitude, year, eventID, associatedUsername)" +
				"VALUES(?,?,?,?,?,?,?,?,?)";

		// Update sql using prepared statement
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, event.getType());
			stmt.setString(2, event.getPersonID());
			stmt.setString(3, event.getCity());
			stmt.setString(4, event.getCountry());
			stmt.setDouble(5, event.getLatitude());
			stmt.setDouble(6, event.getLongitude());
			stmt.setInt(7, event.getYear());
			stmt.setString(8, event.getEventID());
			stmt.setString(9, event.getAssociatedUsername());

			stmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DataAccessException(ex.getMessage());
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
			// Format double values
			NumberFormat formatter = new DecimalFormat("#0.00");
			if (rs.next()) {
				double lat = rs.getFloat("latitude");
				double lon = rs.getFloat("longitude");
				lat = Double.parseDouble(new DecimalFormat("#.####").format(lat));
				lon = Double.parseDouble(new DecimalFormat("#.####").format(lon));
				event = new Event(rs.getString("eventType"),
						rs.getString("personID"),
						rs.getString("city"),
						rs.getString("country"),
						lat,
						lon,
						rs.getInt("year"),
						rs.getString("eventID"),
						rs.getString("associatedUsername")
				);
				return event;
			}
		} catch (SQLException ex) {
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
	 * Finds all Events associated with an input username.
	 *
	 * @param userName The username to search for.
	 * @return A list of all Events associated with the given username.
	 * @throws DataAccessException If error occurred when accessing data.
	 */
	public ArrayList<Event> findByUser(String userName) throws DataAccessException {
		ArrayList<Event> events = new ArrayList<>();
		ResultSet rs = null;
		String sql = "SELECT * FROM event_table WHERE associatedUsername = ?;";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			rs = stmt.executeQuery();
			// Format double values
			NumberFormat formatter = new DecimalFormat("#0.00");
			while (rs.next()) {
				double lat = rs.getFloat("latitude");
				double lon = rs.getFloat("longitude");
				lat = Double.parseDouble(new DecimalFormat("#.####").format(lat));
				lon = Double.parseDouble(new DecimalFormat("#.####").format(lon));
				events.add(new Event(rs.getString("eventType"),
						rs.getString("personID"),
						rs.getString("city"),
						rs.getString("country"),
						lat,
						lon,
						rs.getInt("year"),
						rs.getString("eventID"),
						rs.getString("associatedUsername")
				));
			}
			return events;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			throw new DataAccessException("Error occurred while finding events with associated username " + userName);
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
	 * Finds all Events associated with the given username, then removes them from existing tables.
	 *
	 * @param userName The username to search for.
	 * @throws DataAccessException If error occurred when accessing data.
	 */
	public void deleteByUser(String userName) throws DataAccessException {
		String sql = "DELETE FROM event_table WHERE associatedUsername = ?;";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DataAccessException("SQL Error encountered while deleting all events " +
					"with associatedUsername " + userName + " from table");
		}
	}

	/**
	 * Clears all items from the event table, but does not delete the table itself.
	 *
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
}
