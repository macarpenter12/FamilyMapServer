package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			// This method must be a GET request
			if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
				Headers reqHeaders = exchange.getRequestHeaders();
				String urlPath = exchange.getRequestURI().toString();

				// If URL path is root, default to index.html
				if (urlPath == null || urlPath.equals("/")) {
					urlPath = "/index.html";
				}

				// Determine action based on URI
				String filePath = "web" + urlPath;
				File file = new File(filePath);

				// File does not exist (FILE NOT FOUND)
				if (!file.exists()) {
					String errorHTML = "web/HTML/404.html";
					file = new File(errorHTML);
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
				}
				else {
					// Success (OK)
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				}

				// Populate response body
				OutputStream respBody = exchange.getResponseBody();
				Files.copy(file.toPath(), respBody);
				respBody.close();
			}
			// Not a GET request (BAD REQUEST)
			else {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
			}
		} catch (IOException ex) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			exchange.getResponseBody().close();
			ex.printStackTrace();
			throw ex;
		}
	}

	private String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}
}
