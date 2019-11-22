package service;

import DAO.Database;
import familymap.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonServiceTest {
	private final String REGISTERED_USERNAME = "username";
	private final int NUM_GENERATIONS = 4;
	private final int NUM_GENERATED_PERSONS = (int)Math.pow(2, (NUM_GENERATIONS + 1)) - 1;
	private Database db;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
		db.closeConnection(true);
		new RegisterService().register(new RegisterRequest(REGISTERED_USERNAME, "password","email@email.com",
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
		// Check that proper number of people were generated and found
		PersonService personServ = new PersonService();
		Person[] persons = personServ.findByUsername(REGISTERED_USERNAME);

		assertEquals(NUM_GENERATED_PERSONS, persons.length);
	}

	@Test
	public void negativeTest() throws Exception {
		// Should not be able to find persons for a nonexistent username
		PersonService personServ = new PersonService();
		Person[] persons = personServ.findByUsername("Not a username");

		assertEquals(0, persons.length);
	}
}
