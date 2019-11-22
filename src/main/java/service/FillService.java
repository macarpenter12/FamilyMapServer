package service;

import DAO.Database;
import DAO.EventDAO;
import DAO.PersonDAO;
import DAO.UserDAO;
import exception.DataAccessException;
import familymap.Event;
import familymap.Person;
import familymap.User;
import response.FillResponse;

import java.util.ArrayList;

/**
 * Will populate database with generated family history data. Can be called from Server.
 */
public class FillService {

    /**
     * @param userName       Useranme associated with the person to generate data for.
     * @param numGenerations The number of generations of ancestors to create.
     * @return Returns the response as a serializable object.
     * @throws DataAccessException If an error occurred when accessing the database.
     */
    public FillResponse fill(String userName, int numGenerations) throws DataAccessException {
        Database db = new Database();
        int personsAdded = 0;
        int eventsAdded;
        try {
            // Delete all existing events associated with userName
            EventDAO eDao = new EventDAO(db.openConnection());
            eDao.deleteByUser(userName);
            db.closeConnection(true);

            // Get the user's PersonID
            UserDAO uDao = new UserDAO(db.openConnection());
            User user = uDao.find(userName);
            String userPersonID = user.getPersonID();
            db.closeConnection(true);

            PersonDAO pDao = new PersonDAO(db.openConnection());

			// Get the user's Person object, so we can reinsert after deleting user's data
            Person userPerson = pDao.find(userPersonID);
            userPerson.setFatherID(null);
            userPerson.setMotherID(null);
            userPerson.setSpouseID(null);

            // Generate events for user's person, store for later
            ArrayList<Event> eventsToAdd = DataGenerator.generateEvents(userPerson);
			ArrayList<Event> eventList = new ArrayList<>(eventsToAdd);

            // Delete all existing persons associated with userName
            pDao.deleteByUser(userName);

            // Generate indicated number of generations and insert into database
            ArrayList<Person> genList = new ArrayList<>();
            genList.add(userPerson);
            for (int i = 0; i < numGenerations; ++i) {
                // Initialize next generation
                ArrayList<Person> nextGenList = new ArrayList<>();
                for (Person person : genList) {
                    // Generate parents for each person
                    Person[] persons = DataGenerator.generateParents(person);
                    assert persons != null;

                    // Link parents to person
                    person.setFatherID(persons[0].getPersonID());
                    person.setMotherID(persons[1].getPersonID());

                    // Insert person into database
                    pDao.insert(person);
                    personsAdded++;

                    // Add parents to next generation.
                    nextGenList.add(persons[0]);
                    nextGenList.add(persons[1]);

                    // Generate events for parents, store for later
                    eventList = DataGenerator.generateParentEvents(eventList, person, persons[0], persons[1]);

                }
                // Now that we have finished adding parents for everyone in this generation, move on to next generation
                genList = nextGenList;
            }

            // Insert the remaining generation into the database
            for (Person person : genList) {
                pDao.insert(person);
                personsAdded++;
            }

            db.closeConnection(true);

            // Add stored events to database
            eDao = new EventDAO(db.openConnection());
            for (Event event : eventList) {
                eDao.insert(event);
            }
            eventsAdded = eventList.size();

            db.closeConnection(true);
        } catch (DataAccessException | NullPointerException ex) {
            db.closeConnection(false);
            throw ex;
        }

		return new FillResponse("Successfully added " +
                personsAdded + " persons and " +
                eventsAdded + " events to the database.", true);
    }
}
