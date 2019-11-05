package server;

import DAO.Database;
import com.sun.net.httpserver.HttpServer;
import exception.DataAccessException;
import handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) {
		Database db = new Database();
		try {
			db.openConnection();
			db.createTables();
			db.closeConnection(true);
		} catch (DataAccessException ex) {
			try {
				db.closeConnection(false);
			} catch (DataAccessException closeEx) {
				System.out.println(closeEx.getMessage());
				closeEx.printStackTrace();
			}
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

		Server server = new Server();
		final int DEFAULT_PORT = 8080;
		try {
			server.startServer(DEFAULT_PORT);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		// TODO: Use Executor to shut down server when done.
	}

	private void startServer(int port) throws IOException {
		InetSocketAddress serverAddress = new InetSocketAddress(port);
		HttpServer httpServ = HttpServer.create(serverAddress, 10);
		registerHandlers(httpServ);
		httpServ.start();
		System.out.println("FamilyMapServer listening on port " + port);
	}

	private void registerHandlers(HttpServer httpServ) {
		httpServ.createContext("/", new FileHandler());
		httpServ.createContext("/clear", new ClearHandler());
		httpServ.createContext("/user/register", new RegisterHandler());
		httpServ.createContext("/user/login", new LoginHandler());
		httpServ.createContext("/load", new LoadHandler());
		httpServ.createContext("/fill", new FillHandler());
		httpServ.createContext("/event", new EventHandler());
		httpServ.createContext("/person", new PersonHandler());
	}
}