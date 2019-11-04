package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import request.LoadRequest;
import response.LoadResponse;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

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

				// Success (OK)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

				// Serialize, populate, and return response
				String respData = JsonSerializer.serialize(loadResp);
				OutputStream respBody = exchange.getResponseBody();
				StringStream.writeString(respData, respBody);
				respBody.close();
			} catch (DataAccessException ex) {
				// Something went wrong on our end (INTERNAL ERROR)
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

				// Send error JSON body for Load request
				LoadResponse failResp = new LoadResponse(ex.getMessage(), false);
				String failData = JsonSerializer.serialize(failResp);
				OutputStream failBody = exchange.getResponseBody();
				StringStream.writeString(failData, failBody);
				failBody.close();
			}
		}
		// Not a POST request (BAD REQUEST)
		else {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
		}
	}
}
