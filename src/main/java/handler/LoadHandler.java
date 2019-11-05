package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import request.LoadRequest;
import response.LoadResponse;
import response.Response;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;

public class LoadHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
			// Get the data from the request body
			InputStream reqBody = exchange.getRequestBody();
			String reqData = StringStream.readString(reqBody);

			// Deserialize data to object
			LoadRequest loadReq = JsonSerializer.deserialize(reqData, LoadRequest.class);

			try {
				// Call service to access database
				LoadService loadServ = new LoadService();
				LoadResponse loadResp = loadServ.load(loadReq);

				JsonSerializer.sendResponse(exchange, loadResp);
			} catch (DataAccessException ex) {
				JsonSerializer.sendInternalErrorResponse(exchange, new Response("error: internal error", false));
			}
		}
		// Not a POST request (BAD REQUEST)
		else {
			JsonSerializer.sendResponse(exchange, new Response("error: incorrect request method", false));
		}
	}
}
