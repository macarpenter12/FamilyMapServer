package service;

import DAO.Database;
import familymap.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizeServiceTest {
	private Database db;
	private AuthToken authToken;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
		db.closeConnection(true);
		RegisterService regServ = new RegisterService();
		RegisterResponse regRes = (regServ.register(new RegisterRequest("username", "password", "email@email.com",
				"first", "last", "m")));
		authToken = new AuthToken(regRes.getAuthToken(), regRes.getUserName());
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
		// Valid authToken
		AuthorizeService authServ = new AuthorizeService();
		AuthToken compareToken = authServ.authorize(authToken.getAuthToken());

		assertEquals(authToken, compareToken);
	}

	@Test
	public void negativeTest() throws Exception {
		// Invalid authToken
		AuthorizeService authServ = new AuthorizeService();
		AuthToken compareToken = authServ.authorize("Not a token");

		assertNotEquals(authToken, compareToken);
		assertNull(compareToken);
	}
}
