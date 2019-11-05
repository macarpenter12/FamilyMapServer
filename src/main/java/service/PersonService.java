package service;

import DAO.Database;
import DAO.PersonDAO;
import exception.DataAccessException;
import familymap.Person;

import java.util.ArrayList;

/**
 * Returns the Person associated with the given personID, along with all of that Person's
 * family members. Will help return json data.
 */
public class PersonService {

    /**
     * Searches database for one person given the PersonID.
     *
     * @param personID The PersonID to search for.
     * @return The Person associated with the PersonID provided.
     */
    public Person findByPersonID(String personID) throws DataAccessException {
        Database db = new Database();
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            Person person = pDao.find(personID);
            db.closeConnection(true);
            return person;
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            throw ex;
        }
    }

    /**
     * Searches database for all persons associated with the given userName.
     *
     * @param userName userName to search for.
     * @return ALL persons in the database associated with the userName given.
     * @throws DataAccessException Error accessing data within the service/DAO class.
     */
    public Person[] findByUsername(String userName) throws DataAccessException {
        Database db = new Database();
        try {
            PersonDAO eDao = new PersonDAO(db.openConnection());
            ArrayList<Person> personsAL = eDao.findByUser(userName);
            Person[] persons = new Person[personsAL.size()];
            persons = personsAL.toArray(persons);
            db.closeConnection(true);
            return persons;
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            throw ex;
        }
    }
}
