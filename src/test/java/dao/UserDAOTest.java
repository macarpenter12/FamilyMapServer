package dao;

import DAO.Database;
import DAO.UserDAO;
import exception.DataAccessException;
import familymap.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User testUser;

    @BeforeEach
    public void setup() throws Exception {
        db = new Database();
        testUser = new User("anyuser123", "p@ssw0rd", "johndoe@email.com",
                "John", "Doe", "m", "anyperson123");
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
        User compareUser = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            compareUser = uDao.find(testUser.getUserName());

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNotNull(compareUser);
        assertEquals(testUser, compareUser);
    }

    @Test
    public void insertFail() throws Exception {
        boolean pass = true;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            uDao.insert(testUser);
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            pass = false;
        }
        assertFalse(pass);

        User compareTest = testUser;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareTest = uDao.find(testUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void queryPass() throws Exception {
        User compareUser = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            compareUser = uDao.find(testUser.getUserName());

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNotNull(compareUser);
        assertEquals(testUser, compareUser);
    }

    @Test
    public void queryFail() throws Exception {
        User compareUser = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            StringBuilder strb = new StringBuilder(testUser.getUserName());
            for (int i = strb.length(); i > 0; --i) {
                strb.insert(i, 'x');
            }
            compareUser = uDao.find(strb.toString());

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNull(compareUser);
    }

    @Test
    public void clearTable() throws Exception {
        User compareUserBefore = null;
        User compareUserAfter = null;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            // Insert a user into the db
            uDao.insert(testUser);
            // Should be able to find user
            compareUserBefore = uDao.find(testUser.getUserName());
            uDao.clearTable();
            // Table has been cleared, should not be able to find user
            compareUserAfter = uDao.find(testUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        assertNotNull(compareUserBefore);
        assertNull(compareUserAfter);
        assertNotEquals(compareUserBefore, compareUserAfter);
    }

    @Test
    public void clearFail() throws Exception {
        User compareUser1 = null;
        User compareUser2 = null;
        boolean thrown = false;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            uDao.clearTable();
            compareUser1 = uDao.find(testUser.getUserName());
            uDao.clearTable();
            compareUser2 = uDao.find(testUser.getUserName());
            db.closeConnection(true);

            String sql = "DROP TABLE IF EXISTS user_table";
            Statement stmt = db.getConnection().createStatement();
            stmt.executeUpdate(sql);
            db.closeConnection(true);
            uDao = new UserDAO(db.openConnection());
            uDao.clearTable();

            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            thrown = true;
        }
        assertNull(compareUser1);
        assertNull(compareUser2);
        assertTrue(thrown);
    }
}
