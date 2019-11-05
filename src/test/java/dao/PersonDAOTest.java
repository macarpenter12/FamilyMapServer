package dao;

import DAO.Database;
import DAO.PersonDAO;
import exception.DataAccessException;
import familymap.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person testPerson;

    @BeforeEach
    public void setup() throws Exception {
        db = new Database();
        testPerson = new Person("a1s2d3f4", "anyperson123", "John", "Doe",
                "m", "q1w2e3r4", "z1x2c3v4", "h1j2k3l4");
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
        Person comparePerson = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            comparePerson = pDao.find(testPerson.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNotNull(comparePerson);
        assertEquals(testPerson, comparePerson);
    }

    @Test
    public void insertFail() throws Exception {
        boolean pass = true;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            pDao.insert(testPerson);
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            pass = false;
        }
        assertFalse(pass);

        Person compareTest = testPerson;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            compareTest = pDao.find(testPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void queryPass() throws Exception {
        Person comparePerson = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            comparePerson = pDao.find(testPerson.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNotNull(comparePerson);
        assertEquals(testPerson, comparePerson);
    }

    @Test
    public void queryFail() throws Exception {
        Person comparePerson = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            StringBuilder strb = new StringBuilder(testPerson.getPersonID());
            // Alter the personID
            for (int i = strb.length(); i > 0; --i) {
                strb.insert(i, 'x');
            }
            comparePerson = pDao.find(strb.toString());            // Should not find person

            db.closeConnection(true);
        } catch (DataAccessException ex ) {
            db.closeConnection(false);
        }
        assertNull(comparePerson);
    }

    @Test
    public void clearTable() throws Exception {
        Person comparePersonBefore = null;
        Person comparePersonAfter = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            // Insert a person into the db
            pDao.insert(testPerson);
            // Should be able to find person
            comparePersonBefore = pDao.find(testPerson.getPersonID());
            pDao.clearTable();
            // Table has been cleared, should not be able to find person
            comparePersonAfter = pDao.find(testPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNotNull(comparePersonBefore);
        assertNull(comparePersonAfter);
        assertNotEquals(comparePersonBefore, comparePersonAfter);
    }

    @Test
    public void clearFail() throws Exception {
        Person comparePerson1 = null;
        Person comparePerson2 = null;
        boolean thrown = false;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);

            pDao.insert(testPerson);
            pDao.clearTable();
            comparePerson1 = pDao.find(testPerson.getPersonID());
            pDao.clearTable();
            comparePerson2 = pDao.find(testPerson.getPersonID());
            db.closeConnection(true);

            String sql = "DROP TABLE IF EXISTS person_table";
            Statement stmt = db.getConnection().createStatement();
            stmt.executeUpdate(sql);
            db.closeConnection(true);
            pDao = new PersonDAO(db.openConnection());
            pDao.clearTable();

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            thrown = true;
        }
        assertNull(comparePerson1);
        assertNull(comparePerson2);
        assertTrue(thrown);
    }
}
