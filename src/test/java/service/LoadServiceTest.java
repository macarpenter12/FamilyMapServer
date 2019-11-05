package service;

import DAO.Database;
import DAO.PersonDAO;
import com.google.gson.Gson;
import familymap.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import request.RegisterRequest;
import response.RegisterResponse;

import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
	private final String LOAD_PERSONID = "Sheila_Parker";
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
		// Load data into database
		FileReader fr = new FileReader("./json/example.json");
		LoadRequest data = (new Gson().fromJson(fr, LoadRequest.class));

		LoadService loadServ = new LoadService();
		loadServ.load(data);

		// Should be able to find person
		PersonDAO pDao = new PersonDAO(db.openConnection());
		Person person = pDao.find(LOAD_PERSONID);
		db.closeConnection(true);

		assertNotNull(person);
	}

	@Test
	public void negativeTest() throws Exception {
		// Register new user
		RegisterService regServ = new RegisterService();
		RegisterResponse regRes = (regServ.register(new RegisterRequest("username", "password", "email@email.com",
				"first", "last", "m")));
		String registeredPersonID = regRes.getPersonID();

		// Load data into database
		FileReader fr = new FileReader("./json/example.json");
		LoadRequest data = (new Gson().fromJson(fr, LoadRequest.class));

		LoadService loadServ = new LoadService();
		loadServ.load(data);

		// Should NOT be able to find person because LoadService clears data

		PersonDAO pDao = new PersonDAO(db.openConnection());
		Person person = pDao.find(registeredPersonID);
		db.closeConnection(true);

		assertNull(person);
	}
}
