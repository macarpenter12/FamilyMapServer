package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import familymap.AuthToken;
import familymap.Event;
import response.Response;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.AuthorizeService;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
			try {
				Headers reqHeaders = exchange.getRequestHeaders();
				String tokenJSON = reqHeaders.getFirst("Authorization");
				AuthToken authToken = JsonSerializer.deserialize(tokenJSON, AuthToken.class);
				AuthorizeService authServ = new AuthorizeService();
				if (authServ.authorize(authToken)) {
					String[] param = (exchange.getRequestURI().toString()).split("/");
					EventService eventServ = new EventService();
					// User specified eventID
					if (param.length > 2) {
						String eventID = param[2];
						Event response = eventServ.findByEventID(eventID);

						// Success (OK)
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

						// Send JSON response
						String resData = JsonSerializer.serialize(response);
						OutputStream respBody = exchange.getResponseBody();
						StringStream.writeString(resData, respBody);
						respBody.close();
					}
					// User did not specify, return all events by userName
					else {
						String userName = authToken.getUserName();
						Event[] response = eventServ.findByUsername(userName);

						// Success (OK)
						exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

						// Send JSON response
						String resData = JsonSerializer.serialize(response);
						OutputStream respBody = exchange.getResponseBody();
						StringStream.writeString(resData, respBody);
						respBody.close();
					}
				}
				else {
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
				}
			} catch (DataAccessException ex) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

				Response failRes = new Response(ex.getMessage(), false);
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
