package dao;

import DAO.Database;
import DAO.EventDAO;
import exception.DataAccessException;
import familymap.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
	private Database db;
	private Event testEvent;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		testEvent = new Event("started family map", "person100", "Provo", "USA",
				13.86, 75.24, 2019, "event100", "username100");
		db.openConnection();
		db.createTables();
		db.closeConnection(true);
	}

	@AfterEach
	public void tearDown() throws Exception {
		db.openConnection();
		db.createTables();
		db.clearTables();
		db.closeConnection(true);
	}

	@Test
	public void insertPass() throws Exception {
		Event compareTest = null;
		try {
			// Open connection
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);
			// Insert event
			eDao.insert(testEvent);
			// See if event was inserted
			compareTest = eDao.find(testEvent.getEventID());
			// Commit changes
			db.closeConnection(true);
		} catch (DataAccessException ex) {
			// Error, rollback
			db.closeConnection(false);
		}
		assertNotNull(compareTest);
		assertEquals(testEvent, compareTest);
	}

	@Test
	public void insertFail() throws Exception {
		boolean pass = true;
		try {
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);
			// Insert identical event twice. Should throw exception because eventID must be unique
			eDao.insert(testEvent);
			eDao.insert(testEvent);
			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			pass = false;
		}
		assertFalse(pass);

		// Database should have rolled back. Check that we cannot find event.
		Event compareTest = testEvent;
		try {
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);
			compareTest = eDao.find(testEvent.getEventID());
			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
		}
		assertNull(compareTest);
	}

	@Test
	public void queryPass() throws Exception {
		Event compareEvent = null;
		try {
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);

			eDao.insert(testEvent);
			compareEvent = eDao.find(testEvent.getEventID());

			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
		}
		assertNotNull(compareEvent);
		assertEquals(testEvent, compareEvent);
	}

	@Test
	public void queryFail() throws Exception {
		Event compareEvent = null;
		try {
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);

			eDao.insert(testEvent);
			StringBuilder strb = new StringBuilder(testEvent.getEventID());
			// Alter the personID
			for (int i = strb.length(); i > 0; --i) {
				strb.insert(i, 'x');
			}
			compareEvent = eDao.find(strb.toString());            // Should not find person

			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
		}
		assertNull(compareEvent);
	}

	@Test
	public void clearTable() throws Exception {
		Event compareEventBefore = null;
		Event compareEventAfter = null;
		try {
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);

			// Insert a person into the db
			eDao.insert(testEvent);
			// Should be able to find person
			compareEventBefore = eDao.find(testEvent.getEventID());
			eDao.clearTable();
			// Table has been cleared, should not be able to find person
			compareEventAfter = eDao.find(testEvent.getEventID());
			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
		}
		assertNotNull(compareEventBefore);
		assertNull(compareEventAfter);
		assertNotEquals(compareEventBefore, compareEventAfter);
	}

	@Test
	public void clearFail() throws Exception {
		Event compareEvent = null;
		Event compareEvent2 = null;
		boolean thrown = false;
		try {
			Connection conn = db.openConnection();
			EventDAO eDao = new EventDAO(conn);

			eDao.insert(testEvent);
			eDao.clearTable();
			compareEvent = eDao.find(testEvent.getEventID());
			eDao.clearTable();
			compareEvent2 = eDao.find(testEvent.getEventID());
			db.closeConnection(true);

			String sql = "DROP TABLE IF EXISTS event_table";
			Statement stmt = db.getConnection().createStatement();
			stmt.executeUpdate(sql);
			db.closeConnection(true);
			eDao = new EventDAO(db.openConnection());
			eDao.clearTable();

			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			thrown = true;
		}
		assertNull(compareEvent);
		assertNull(compareEvent2);
		assertTrue(thrown);
	}
}
