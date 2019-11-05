package service;

import DAO.Database;
import DAO.UserDAO;
import exception.DataAccessException;
import familymap.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
	private final String REG_USERNAME = "username";
	private final String REG_PASSWORD = "password";
	private Database db;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
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
	public void positiveTest() throws Exception {
		// Find a user that has been registered
		RegisterService regServ = new RegisterService();
		RegisterRequest regReq = new RegisterRequest(REG_USERNAME, REG_PASSWORD, "email@email.com",
				"first", "last", "m");
		RegisterResponse regRes = regServ.register(regReq);

		UserDAO uDao = new UserDAO(db.openConnection());
		User user = uDao.find(REG_USERNAME);
		db.closeConnection(true);

		assertNotNull(user);
	}

	@Test
	public void negativeTest() throws Exception {
		boolean thrown = false;
		try {
			// Register a user with a username already taken
			RegisterService regServ = new RegisterService();
			RegisterRequest regReq = new RegisterRequest(REG_USERNAME, REG_PASSWORD, "email@email.com",
					"first", "last", "m");
			RegisterRequest regReq2 = new RegisterRequest(REG_USERNAME, "p@ssw0rd", "other@email.com",
					"Sally", "last", "f");
			RegisterResponse regRes = regServ.register(regReq);
			// Oops, used the same username, DataAccessException is thrown here
			RegisterResponse regRes2 = regServ.register(regReq2);
			String token = regRes.getAuthToken();
			String token2 = regRes2.getAuthToken();
		} catch (DataAccessException ex) {
			thrown = true;
		}

		assertTrue(thrown);
	}
}
