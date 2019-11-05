package service;

import DAO.AuthTokenDAO;
import DAO.Database;
import exception.DataAccessException;
import familymap.AuthToken;

public class AuthorizeService {

	public AuthToken authorize(AuthToken authToken) throws DataAccessException {
		Database db = new Database();
		try {
			String token = authToken.getAuthToken();
			// Search database for authToken, compare its username and token with the given authToken
			AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
			AuthToken compareToken = aDao.find(token);
			db.closeConnection(true);
			return compareToken;
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			throw ex;
		}
	}

	public AuthToken authorize(String token) throws DataAccessException {
		Database db = new Database();
		try {
			// Search database for authToken, compare its username and token with the given authToken
			AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
			AuthToken compareToken = aDao.find(token);
			db.closeConnection(true);
			return compareToken;
		} catch (DataAccessException ex) {
			db.closeConnection(false);
			throw ex;
		}
	}
}
