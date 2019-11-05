package service;

import DAO.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
	private final String REGISTERED_USERNAME = "username";
	private final String REGISTERED_PASSWORD = "password";
	private Database db;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
		db.closeConnection(true);
		new RegisterService().register(new RegisterRequest(REGISTERED_USERNAME, REGISTERED_PASSWORD,"email@email.com",
				"first", "last", "m"));
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
		// Login with valid credentials
		LoginService logServ = new LoginService();
		LoginRequest logReq= new LoginRequest(REGISTERED_USERNAME, REGISTERED_PASSWORD);
		LoginResponse logRes = logServ.login(logReq);
		String token = logRes.getAuthToken();

		assertNotNull(token);
	}

	@Test
	public void negativeTest() throws Exception {
		// Login with invalid credentials
		LoginService logServ = new LoginService();
		LoginRequest logReq= new LoginRequest("Not a username", "notapassword");
		LoginResponse logRes = logServ.login(logReq);
		String token = logRes.getAuthToken();

		assertNull(token);
	}
}
