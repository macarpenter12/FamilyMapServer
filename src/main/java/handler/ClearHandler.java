package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import response.ClearResponse;
import response.Response;
import serializer.JsonSerializer;
import service.ClearService;

import java.io.IOException;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            try {
                // Call the service to access database
                ClearService clearServe = new ClearService();
                clearServe.clear();

                ClearResponse clrResp = new ClearResponse("Clear succeeded", true);
                JsonSerializer.sendResponse(exchange, clrResp);
            } catch (DataAccessException ex) {
                // Send error JSON body for Clear request
                ClearResponse failResp = new ClearResponse(ex.getMessage(), false);
                JsonSerializer.sendInternalErrorResponse(exchange, failResp);
            }
        }
        // Not a POST request (BAD REQUEST)
        else {
            JsonSerializer.sendResponse(exchange, new Response("error: incorrect request method", false));
        }
    }
}
