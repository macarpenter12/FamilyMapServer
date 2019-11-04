package service;

import DAO.Database;
import exception.DataAccessException;

/**
 * Deletes all data currently in the database. Server can call a function that will call this class.
 */
public class ClearService {
	private Database db = new Database();

	public void clear() throws DataAccessException {
		try {
			db.openConnection();
			db.clearTables();
			db.closeConnection(true);
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			throw ex;
		}
	}
}
