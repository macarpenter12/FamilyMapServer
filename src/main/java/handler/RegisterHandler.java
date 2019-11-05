package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import familymap.AuthToken;
import request.RegisterRequest;
import response.RegisterResponse;
import response.Response;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;

public class RegisterHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
			// Get the data from the request body
			InputStream reqBody = exchange.getRequestBody();
			String reqData = StringStream.readString(reqBody);

			// Deserialize data to request object
			RegisterRequest registerReq = JsonSerializer.deserialize(reqData, RegisterRequest.class);

			try {
				RegisterService registerServ = new RegisterService();
				RegisterResponse registerRes = registerServ.register(registerReq);

				// Generate Authentication header with token
				AuthToken authToken = new AuthToken(registerRes.getAuthToken(), registerRes.getUserName());
				String token = authToken.getAuthToken();
				Headers resHeaders = exchange.getResponseHeaders();
				resHeaders.add("Authorization", JsonSerializer.serialize(token));

				JsonSerializer.sendResponse(exchange, registerRes);
			} catch (DataAccessException ex) {
				JsonSerializer.sendInternalErrorResponse(exchange, new Response("error: internal error", false));
			}
		}
		else {
			JsonSerializer.sendResponse(exchange, new Response("error: incorrect request method", false));
		}
	}
}
