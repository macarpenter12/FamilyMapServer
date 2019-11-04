package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import familymap.AuthToken;
import request.RegisterRequest;
import response.RegisterResponse;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

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

				// Generate Authentication header with token and username
				AuthToken authToken = new AuthToken(registerRes.getAuthToken(), registerRes.getUserName());
				Headers resHeaders = exchange.getResponseHeaders();
				resHeaders.add("Authorization", JsonSerializer.serialize(authToken));

				// Success (OK)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

				// Serialize, populate, and return response
				String respData = JsonSerializer.serialize(registerRes);
				OutputStream respBody = exchange.getResponseBody();
				StringStream.writeString(respData, respBody);
				respBody.close();
			} catch (DataAccessException ex) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

				RegisterResponse failRes = new RegisterResponse(ex.getMessage(), false);
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
