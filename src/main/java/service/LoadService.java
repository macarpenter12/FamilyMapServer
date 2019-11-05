package service;

import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import exception.DataAccessException;
import familymap.Event;
import familymap.Person;
import familymap.User;
import request.LoadRequest;
import response.LoadResponse;

/**
 * Populates database with given array of Users, Persons, and Events. DOES NOT clear the database before, that is
 * left to the Clear Handler.
 */
public class LoadService {

	/**
	 * @param loadReq The json object containing the Users, Persons, and Events to add to database.
	 */
	public LoadResponse load(LoadRequest loadReq) throws DataAccessException {
		User[] users = loadReq.getUsers();
		Person[] persons = loadReq.getPersons();
		Event[] events = loadReq.getEvents();

		Database db = new Database();
		try {
			// Clear all data from the database
			new ClearService().clear();

			// Insert each type of data to database, committing changes after each type
			UserDAO uDao = new UserDAO(db.openConnection());
			for (int i = 0; i < users.length; ++i) {
				uDao.insert(users[i]);
			}
			db.closeConnection(true);

			PersonDAO pDao = new PersonDAO(db.openConnection());
			for (int i = 0; i < persons.length; ++i) {
				pDao.insert(persons[i]);
			}
			db.closeConnection(true);

			EventDAO eDao = new EventDAO(db.openConnection());
			for (int i = 0; i < events.length; ++i) {
				eDao.insert(events[i]);
			}
			db.closeConnection(true);

		} catch (DataAccessException ex) {
			db.closeConnection(false);
			throw ex;
		}

		// Generate response object to send back
		return new LoadResponse(users.length, persons.length, events.length, true);
	}
}
