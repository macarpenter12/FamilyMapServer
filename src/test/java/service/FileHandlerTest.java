package service;

import DAO.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileHandlerTest {
	Database db = new Database();

	@BeforeEach
	public void setup() throws Exception {
		db = new Database();
		db.openConnection();
		db.createTables();
		db.closeConnection(true);
	}

	@AfterEach
	public void tearDown() throws Exception {
		db.openConnection();
		db.clearTables();
		db.closeConnection(true);
	}

	@Test
	public void get() throws Exception {
//		server.startServer(8080);

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
