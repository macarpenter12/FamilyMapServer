package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import familymap.AuthToken;
import familymap.Person;
import response.DataResponse;
import response.PersonResponse;
import response.Response;
import serializer.JsonSerializer;
import service.AuthorizeService;
import service.PersonService;

import java.io.IOException;

public class PersonHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
	if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
	  try {
		Headers reqHeaders=exchange.getRequestHeaders();
		String token=reqHeaders.getFirst("Authorization");
		AuthorizeService authServ=new AuthorizeService();
		AuthToken compareToken=authServ.authorize(token);
		// If authorization service found auth token for that username, they are authorized
		if (compareToken != null) {
		  String[] param=(exchange.getRequestURI().toString()).split("/");
		  PersonService personServ=new PersonService();
		  // User specified eventID
		  if (param.length > 2) {
			String eventID=param[2];
			Person response=personServ.findByPersonID(eventID);

			// Check that person with ID is associated with user
			String userName=compareToken.getUserName();
			if (!userName.equals(response.getAssociatedUsername())) {
			  Response responseError=new Response("error: not authorized", false);
			  JsonSerializer.sendResponse(exchange, responseError);
			}
			else {
			  JsonSerializer.sendResponse(exchange, response);
			}
		  }
		  // User did not specify, return all events by userName
		  else {
			String userName=compareToken.getUserName();
			Person[] persons=personServ.findByUsername(userName);
			PersonResponse response=new PersonResponse(persons);

			JsonSerializer.sendResponse(exchange, response);
		  }
		}
		else {
		  JsonSerializer.sendResponse(exchange, new Response("error: unauthorized", false));
		}
	  } catch (DataAccessException ex) {
		Response failRes=new Response("error: internal error", false);
		JsonSerializer.sendInternalErrorResponse(exchange, failRes);
	  }
	}
	else {
	  JsonSerializer.sendResponse(exchange, new Response("error: incorrect request method", false));
	}
  }
}
