package service;

import DAO.Database;
import familymap.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FillServiceTest {
	private Database db;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
		db.clearTables();
		db.closeConnection(true);
		new RegisterService().register(new RegisterRequest("username", "password","email@email.com",
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
		FillService fillServ = new FillService();
		fillServ.fill("username", 4);
		PersonService personServ = new PersonService();
		Person[] persons = personServ.findByUsername("username");

		assertEquals(31, persons.length);
	}

	@Test
	public void negativeTest() throws Exception {
		boolean thrown = false;
		try {
			FillService fillServ = new FillService();
			fillServ.fill("bob", 4);
		} catch (NullPointerException ex) {
			thrown = true;
		}

		assertTrue(thrown);
	}
}
