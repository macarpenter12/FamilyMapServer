package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import response.FillResponse;
import response.Response;
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

				JsonSerializer.sendResponse(exchange, fillRes);

			} catch (DataAccessException ex) {
				JsonSerializer.sendInternalErrorResponse(exchange, new Response("error: internal error", false));
			} catch (NullPointerException ex) {
				JsonSerializer.sendResponse(exchange, new Response("error: user does not exist", false));
			}
		}
		else {
			JsonSerializer.sendResponse(exchange, new Response("error: incorrect request method", false));
		}
	}
}
