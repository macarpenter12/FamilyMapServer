package dao;

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
    private Server server;
    private Event testEvent;

    @BeforeEach
    public void setup() throws Exception {
        server = new Server();
        testEvent = new Event("a1s2d3f4", "anyuser123", "anyperson321",
                32.69, -114.63, "USA", "Yuma",
                "someEvent", 2019);
        server.openConnection();
        server.createTables();
        server.closeConnection(true);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.openConnection();
        server.clearTables();
        server.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
        Event compareTest = null;
        try {
            // Open connection
            Connection conn = server.openConnection();
            EventDAO eDao = new EventDAO(conn);
            // Insert event
            eDao.insert(testEvent);
            // See if event was inserted
            compareTest = eDao.find(testEvent.getEventID());
            // Commit changes
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            // Error, rollback
            server.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(testEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean pass = true;
        try {
            Connection conn = server.openConnection();
            EventDAO eDao = new EventDAO(conn);
            // Insert identical event twice. Should throw exception because eventID must be unique
            eDao.insert(testEvent);
            eDao.insert(testEvent);
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
            pass = false;
        }
        assertFalse(pass);

        // Database should have rolled back. Check that we cannot find event.
        Event compareTest = testEvent;
        try {
            Connection conn = server.openConnection();
            EventDAO eDao = new EventDAO(conn);
            compareTest = eDao.find(testEvent.getEventID());
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNull(compareTest);
    }
}
