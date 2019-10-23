package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileServiceTest {
	Server server = new Server();

	@BeforeEach
	public void setup() throws Exception {
		server = new Server();
		server.openConnection();
		server.createTables();
		server.closeConnection(true);
	}

	@AfterEach
	public void tearDown() throws Exception {
		server.openConnection();
		server.clearTables();
		server.closeConnection(true);
	}

	@Test
	public void get() throws Exception {
		server.startServer(8080);

		URL url = new URL("http://localhost:8080/");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setReadTimeout(5000);
		connection.setRequestMethod("GET");

		// Set HTTP request headers, if necessary
		// connection.addRequestProperty("Accept", "text/html");
		connection.addRequestProperty("Authorization", "afj232hj2332");

		connection.connect();

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			// Get HTTP response headers, if necessary
			// Map<String, List<String>> headers = connection.getHeaderFields();

			// OR

			//connection.getHeaderField("Content-Length");

			InputStream responseBody = connection.getInputStream();
			// Read and process response body from InputStream ...

		}
		else {
			// SERVER RETURNED AN HTTP ERROR
		}

	}
}
