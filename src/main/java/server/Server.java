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
	}

}