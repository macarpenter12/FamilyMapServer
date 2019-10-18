package dao;

import DAO.PersonDAO;
import exception.DataAccessException;
import familymap.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Server server;
    private Person testPerson;

    @BeforeEach
    public void setup() throws Exception {
        server = new Server();
        testPerson = new Person("a1s2d3f4", "anyperson123", "John", "Doe",
                "m", "q1w2e3r4", "z1x2c3v4", "h1j2k3l4");
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
        Person comparePerson = null;
        try {
            Connection conn = server.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            comparePerson = pDao.find(testPerson.getPersonID());

            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNotNull(comparePerson);
        assertEquals(testPerson, comparePerson);
    }

    @Test
    public void insertFail() throws Exception {
        boolean pass = true;
        try {
            Connection conn = server.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            pDao.insert(testPerson);
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
            pass = false;
        }
        assertFalse(pass);

        Person compareTest = testPerson;
        try {
            Connection conn = server.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            compareTest = pDao.find(testPerson.getPersonID());
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearTable() throws Exception {
        Person comparePersonBefore = null;
        Person comparePersonAfter = null;
        try {
            Connection conn = server.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            // Insert a person into the db
            pDao.insert(testPerson);
            // Should be able to find person
            comparePersonBefore = pDao.find(testPerson.getPersonID());
            pDao.clearTable();
            // Table has been cleared, should not be able to find person
            comparePersonAfter = pDao.find(testPerson.getPersonID());
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNotNull(comparePersonBefore);
        assertNull(comparePersonAfter);
        assertNotEquals(comparePersonBefore, comparePersonAfter);
    }
}
