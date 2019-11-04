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
     * @param eventID The json object to process.
     * @return Returns the response as a json object.
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

    public Event[] findByUsername(String userName) throws DataAccessException {
        Database db = new Database();
        try {
            EventDAO eDao = new EventDAO(db.openConnection());
            ArrayList<Event> eventsAL= eDao.findByUser(userName);
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
