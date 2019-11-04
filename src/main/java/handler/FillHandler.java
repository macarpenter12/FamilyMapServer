package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import response.FillResponse;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.FillService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
			String[] param = (exchange.getRequestURI().toString()).split("/");
			String userName = param[2];
			int numGenerations = 4;		// If number of generations is not specified, default 4
			if (param.length > 3) {
				numGenerations = Integer.parseInt(param[3]);
			}

			try {
				// Call service to database
				FillService fillServ = new FillService();
				FillResponse fillRes = fillServ.fill(userName, numGenerations);

				// Success (OK)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

				// Serialize, populate, and return response
				String respData = JsonSerializer.serialize(fillRes);
				OutputStream respBody = exchange.getResponseBody();
				StringStream.writeString(respData, respBody);
				respBody.close();

			} catch (DataAccessException ex) {
				// (INTERNAL ERROR)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

				// Send error body
				FillResponse failRes = new FillResponse(ex.getMessage(), false);
				String failData = JsonSerializer.serialize(failRes);
				OutputStream failBody = exchange.getResponseBody();
				StringStream.writeString(failData, failBody);
				failBody.close();
			}
		}
		else {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
		}
	}
}
