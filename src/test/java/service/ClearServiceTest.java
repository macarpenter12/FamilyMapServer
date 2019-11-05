package service;

import DAO.Database;
import DAO.UserDAO;
import familymap.AuthToken;
import familymap.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.AuthorizedRequest;
import request.RegisterRequest;
import response.RegisterResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
	private String token = null;
	private Database db;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
		db.closeConnection(true);
		RegisterService regServ = new RegisterService();
		RegisterResponse regRes = regServ.register(new RegisterRequest("username", "password","email@email.com",
				"first", "last", "m"));
		token = regRes.getAuthToken();
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
		// Should not be able to find user after database has cleared
		UserDAO uDao = new UserDAO(db.openConnection());
		User user = uDao.find("username");
		db.closeConnection(true);

		ClearService clearServ = new ClearService();
		clearServ.clear();

		uDao = new UserDAO(db.openConnection());
		User user2 = uDao.find("username");
		db.closeConnection(true);

		assertNotNull(user);
		assertNull(user2);
	}

	@Test
	public void positiveTest2() throws Exception {
		// Should not be able to find auth token after database has cleared
		AuthorizeService authServ = new AuthorizeService();
		AuthToken authToken = authServ.authorize(token);

		new ClearService().clear();

		AuthToken compareToken = authServ.authorize(token);

		assertNotNull(authToken);
		assertNull(compareToken);
	}
}
