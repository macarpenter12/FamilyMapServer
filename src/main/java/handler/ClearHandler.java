package handler;

import exception.DataAccessException;
import response.ClearResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import serializer.JsonSerializer;
import serializer.StringStream;
import service.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
            try {
                // Call the service to access database
                ClearService clearServe = new ClearService();
                clearServe.clear();

                // Success (OK)
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // Populate response body
                ClearResponse clrResp = new ClearResponse("Clear succeeded", true);
                String respData = JsonSerializer.serialize(clrResp);
                OutputStream respBody = exchange.getResponseBody();
                StringStream.writeString(respData, respBody);
                respBody.close();
            } catch (DataAccessException ex) {
                // Something went wrong on our end (INTERNAL ERROR)
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

                // Send error JSON body for Clear request
                ClearResponse failResp = new ClearResponse(ex.getMessage(), false);
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
