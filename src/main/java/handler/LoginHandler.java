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
import java.io.OutputStream;
import java.net.HttpURLConnection;

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
				// Call service to access database
				LoginService loginServ = new LoginService();
				LoginResponse loginRes = loginServ.login(loginReq);

				// Generate Authentication header with token and username
				AuthToken authToken = new AuthToken(loginRes.getAuthToken(), loginRes.getUserName());
				Headers resHeaders = exchange.getResponseHeaders();
				resHeaders.add("Authorization", JsonSerializer.serialize(authToken));

				// Success (OK)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

				// Serialize, populate, and return response
				String respData = JsonSerializer.serialize(loginRes);
				OutputStream respBody = exchange.getResponseBody();
				StringStream.writeString(respData, respBody);
				respBody.close();
			} catch (DataAccessException ex) {
				// Something went wrong on our end (INTERNAL ERROR)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

				// Send error body for Login request as JSON object
				LoginResponse failRes = new LoginResponse(ex.getMessage(), false);
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
