package service;

import DAO.Database;
import exception.DataAccessException;

import java.net.HttpURLConnection;

/**
 * Deletes all data currently in the database. Server can call a function that will call this class.
 */
public class ClearService {
    Database db = new Database();

    /**
     * @param request The json object to process.
     * @return Returns the response as a json object.
     */
    public void clear(String request) throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }
}
