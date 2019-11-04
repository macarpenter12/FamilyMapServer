package dao;

import DAO.Database;
import DAO.EventDAO;
import exception.DataAccessException;
import familymap.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.sql.Connection;

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
}
