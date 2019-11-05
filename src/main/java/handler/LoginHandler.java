package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import familymap.AuthToken;
import request.LoginRequest;
import response.LoginResponse;
import response.Response;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.LoginService;

import java.io.IOException;
import java.io.InputStream;

public class LoginHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
			// Get the data from the request body
			InputStream reqBody = exchange.getRequestBody();
			String reqData = StringStream.readString(reqBody);

			// Deserialize data to request object
			LoginRequest loginReq = JsonSerializer.deserialize(reqData, LoginRequest.class);

			try {
				LoginService loginServ = new LoginService();
				LoginResponse loginRes = loginServ.login(loginReq);

				// Generate Authentication header with token
				AuthToken authToken = new AuthToken(loginRes.getAuthToken(), loginRes.getUserName());
				String token = authToken.getAuthToken();
				Headers resHeaders = exchange.getResponseHeaders();
				resHeaders.add("Authorization", JsonSerializer.serialize(token));

				JsonSerializer.sendResponse(exchange, loginRes);
			} catch (DataAccessException ex) {
				JsonSerializer.sendInternalErrorResponse(exchange, new Response("error: internal error", false));
			}
		}
		else {
			JsonSerializer.sendResponse(exchange, new Response("error: incorrect request method", false));
		}
	}
}
