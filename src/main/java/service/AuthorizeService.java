package service;

import DAO.AuthTokenDAO;
import DAO.Database;
import exception.DataAccessException;
import familymap.AuthToken;

public class AuthorizeService {

	public boolean authorize(AuthToken authToken) throws DataAccessException {
		Database db = new Database();
		String token = authToken.getAuthToken();

		// Search database for authToken, compare its username and token with the given authToken
		AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
		AuthToken compareToken = aDao.find(token);
		boolean found = authToken.equals(compareToken);
		db.closeConnection(true);
		return found;
	}
}
