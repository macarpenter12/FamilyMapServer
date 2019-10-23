package dao;

import DAO.UserDAO;
import exception.DataAccessException;
import familymap.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Server server;
    private User testUser;

    @BeforeEach
    public void setup() throws Exception {
        server = new Server();
        testUser = new User("anyuser123", "p@ssw0rd", "johndoe@email.com",
                "John", "Doe", "m");
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
        User compareUser = null;
        try {
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            compareUser = uDao.find(testUser.getUsername());

            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNotNull(compareUser);
        assertEquals(testUser, compareUser);
    }

    @Test
    public void insertFail() throws Exception {
        boolean pass = true;
        try {
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            uDao.insert(testUser);
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
            pass = false;
        }
        assertFalse(pass);

        User compareTest = testUser;
        try {
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);
            compareTest = uDao.find(testUser.getUsername());
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void queryPass() throws Exception {
        User compareUser = null;
        try {
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            compareUser = uDao.find(testUser.getUsername());

            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNotNull(compareUser);
        assertEquals(testUser, compareUser);
    }

    @Test
    public void queryFail() throws Exception {
        User compareUser = null;
        try {
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            StringBuilder strb = new StringBuilder(testUser.getUsername());
            for (int i = strb.length(); i > 0; --i) {
                strb.insert(i, 'x');
            }
            compareUser = uDao.find(strb.toString());

            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
        }
        assertNull(compareUser);
    }

    @Test
    public void clearTable() throws Exception {
        User compareUserBefore = null;
        User compareUserAfter = null;
        try {
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);

            // Insert a user into the db
            uDao.insert(testUser);
            // Should be able to find user
            compareUserBefore = uDao.find(testUser.getUsername());
            uDao.clearTable();
            // Table has been cleared, should not be able to find user
            compareUserAfter = uDao.find(testUser.getUsername());
            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
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
            Connection conn = server.openConnection();
            UserDAO uDao = new UserDAO(conn);

            uDao.insert(testUser);
            uDao.clearTable();
            compareUser1 = uDao.find(testUser.getUsername());
            uDao.clearTable();
            compareUser2 = uDao.find(testUser.getUsername());

            String sql = "DROP TABLE IF EXISTS user_table";
            Statement stmt = server.getConnection().createStatement();
            stmt.executeUpdate(sql);
            uDao.clearTable();

            server.closeConnection(true);
        } catch (DataAccessException ex) {
            server.closeConnection(false);
            thrown = true;
        }
        assertNull(compareUser1);
        assertNull(compareUser2);
        assertTrue(thrown);
    }
}
