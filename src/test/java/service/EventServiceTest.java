package service;

import DAO.Database;
import familymap.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventServiceTest {
	private final String REGISTERED_USERNAME = "username";
	private final int NUM_GENERATIONS = 4;
	private final int NUM_GENERATED_PERSONS = (int)Math.pow(2, (NUM_GENERATIONS + 1)) - 1;
	private final int NUM_GENERATED_EVENTS = (NUM_GENERATED_PERSONS * 3) - 2;
	private Database db;

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
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
		// Check that the proper number of events were generated and found
		EventService eventServ = new EventService();
		Event[] events = eventServ.findByUsername(REGISTERED_USERNAME);

		assertEquals(NUM_GENERATED_EVENTS, events.length);
	}

	@Test
	public void negativeTest() throws Exception {
		// Should not be able to find events for a nonexistent username
		EventService eventServ = new EventService();
		Event[] events = eventServ.findByUsername("Not a username");

		assertEquals(0, events.length);
	}
}
