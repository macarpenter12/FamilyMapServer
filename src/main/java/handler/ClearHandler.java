package handler;

import DAO.Database;
import response.ClearResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.DataAccessException;
import serializer.JsonSerializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().toUpperCase().equals("POST")) {

            // Success (OK)
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            // Populate response body
            ClearResponse clrResp = new ClearResponse("Tables cleared successfully", true);
            String respData = JsonSerializer.serialize(clrResp);
            OutputStream respBody = exchange.getResponseBody();
            writeString(respData, respBody);
            respBody.close();
        }
        // Not a POST request (BAD REQUEST)
        else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }

    }

    /**
     * Writes the given String to a given OutputStream
     *
     * @param str String to be written
     * @param os  OutputStream to be written to
     * @throws IOException Error occurred while writing to stream
     */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
