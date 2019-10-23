package server;

import com.sun.net.httpserver.HttpServer;
import exception.DataAccessException;
import familymap.User;
import handler.FileHandler;
import handler.ClearHandler;
import service.FileService;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.sql.*;

public class Server {
	private Connection conn;

	public static void main(String[] args) {
		Server server = new Server();
		try {
			server.startServer(8080);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		// TODO: Use Executor to shut down server when done.
	}

	public Connection getConnection() throws DataAccessException {
		if (conn == null) {
			return conn;
		}
		else {
			return openConnection();
		}
	}

	public void startServer(int port) throws IOException {
		InetSocketAddress serverAddress = new InetSocketAddress(port);
		HttpServer httpServ = HttpServer.create(serverAddress, 10);
		registerHandlers(httpServ);
		httpServ.start();
		System.out.println("FamilyMapServer listening on port " + port);
	}

	private void registerHandlers(HttpServer httpServ) {
		httpServ.createContext("/", new FileHandler());
		httpServ.createContext("/clear", new ClearHandler());
	}

	public Connection openConnection() throws DataAccessException {
		try {
			final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
			conn = DriverManager.getConnection(CONNECTION_URL);
			conn.setAutoCommit(false);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DataAccessException("Unable to open connection to database");
		}

		return conn;
	}

	public void closeConnection(boolean commit) throws DataAccessException {
		try {
			// Commit changes to database (save changes)
			if (commit) {
				conn.commit();
			}
			// Rollback changes made during connection (don't save changes)
			else {
				conn.rollback();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DataAccessException("Unable to close database connection");
		}
	}

	public void createTables() throws DataAccessException {
		// Create the database tables, if they don't exist already
		try (Statement stmt = conn.createStatement()) {
			// Person table
			String sql = "CREATE TABLE IF NOT EXISTS person_table " +
					"(" +
					"personID text not null unique, " +
					"username text not null, " +
					"firstName text not null, " +
					"lastName text not null, " +
					"gender text not null, " +
					"fatherID text not null, " +
					"motherID text not null, " +
					"spouseID text not null, " +
					"primary key (personID), " +
					"foreign key (username) references user_table(username)" +
					")";
			stmt.executeUpdate(sql);

			// Event table
			sql = "CREATE TABLE IF NOT EXISTS event_table " +
					"(" +
					"eventID text not null unique, " +
					"username text not null, " +
					"personID text not null, " +
					"latitude float not null, " +
					"longitude float not null, " +
					"country text not null, " +
					"city text not null, " +
					"type text not null, " +
					"year int not null, " +
					"primary key (eventID), " +
					"foreign key (username) references user_table(username), " +
					"foreign key (personID) references person_table(personID)" +
					")";
			stmt.executeUpdate(sql);

			// User table
			sql = "CREATE TABLE IF NOT EXISTS user_table " +
					"(" +
					"username text not null unique, " +
					"password text not null, " +
					"email text not null, " +
					"firstName text not null, " +
					"lastName text not null, " +
					"gender text not null, " +
					"primary key (username)" +
					")";
			stmt.executeUpdate(sql);

			// AuthToken table
			sql = "CREATE TABLE IF NOT EXISTS authtoken_table " +
					"(" +
					"authtoken text not null unique, " +
					" username text not null, " +
					"primary key (authtoken), " +
					"foreign key (username) references user_table(username)" +
					")";
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			throw new DataAccessException("SQL Error encountered while creating tables");
		}
	}

	public void clearTables() throws DataAccessException {
		try (Statement stmt = conn.createStatement()) {
			String sql = "DELETE FROM person_table";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM event_table";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM user_table";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM authtoken_table";
			stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			throw new DataAccessException("SQL Error encountered while clearing tables");
		}
	}

	/**
	 * Will call UserDAO to handle insertion of a new User into the database.
	 *
	 * @param user The User object to be added to the database.
	 * @return If the operation was successful.
	 */
	public boolean registerUser(User user) {
		return false;
	}

	/**
	 * Will call UserDAO to authenticate the user and give them an Authentication Token, which will
	 * grant them access to further database operations without needing to login again.
	 *
	 * @param username The username associated with an existing User in the database.
	 * @param password The password for the User object associated with the given username in the DB.
	 * @return An Authentication Token granting access to the account, or null if incorrect credentials/error
	 */
	public AuthToken login(String username, String password) {
		return null;
	}

	/**
	 * Will randomly generate Person and Event objects and insert them into the database.
	 *
	 * @param user        The User to generate family history information for
	 * @param generations The number of generations to generate besides the user (i.e., parents = 1,
	 *                    grandparents = 2, etc...)
	 */
	public void generateFamilyHistory(User user, int generations) {
	}
}