package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			if (httpExchange.getRequestMethod().toUpperCase().equals("POST")) {

				throw new IOException();
			}
		} catch (IOException ex) {
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			httpExchange.getResponseBody().close();
			ex.printStackTrace();
			throw ex;
		}
	}
}
