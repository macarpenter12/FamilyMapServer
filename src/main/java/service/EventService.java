package service;

import DAO.Database;
import DAO.EventDAO;
import exception.DataAccessException;
import familymap.Event;

import java.util.ArrayList;

/**
 * Returns the Event associated with the given eventID, along with ALL Events belonging to the
 * current user AND family members of the user. Will help return json data.
 */
public class EventService {

	/**
	 * Searches database for one event given the EventID.
	 *
	 * @param eventID The ID to search for.
	 * @return The event associated with the EventID provided.
	 */
	public Event findByEventID(String eventID) throws DataAccessException {
		Database db = new Database();
		try {
			EventDAO eDao = new EventDAO(db.openConnection());
			Event event = eDao.find(eventID);

			db.closeConnection(true);
			return event;
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			throw ex;
		}
	}

    /**
     * Searches database for all events associated with the given userName.
     *
     * @param userName userName to search for.
     * @return ALL events in the database associated with the userName given.
     * @throws DataAccessException Error accessing data within the service/DAO class.
     */
	public Event[] findByUsername(String userName) throws DataAccessException {
		Database db = new Database();
		try {
			EventDAO eDao = new EventDAO(db.openConnection());
			ArrayList<Event> eventsAL = eDao.findByUser(userName);
			Event[] events = new Event[eventsAL.size()];
			events = eventsAL.toArray(events);
			db.closeConnection(true);
			return events;
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			throw ex;
		}
	}
}
