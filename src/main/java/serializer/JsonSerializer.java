package serializer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import response.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class JsonSerializer {

	public static <T> T deserialize(String value, Class<T> returnType) {
		return (new Gson()).fromJson(value, returnType);
	}

	public static String serialize(Object obj) {
		return (new Gson()).toJson(obj);
	}

	public static void sendResponse(HttpExchange exchange, Object response) throws IOException {
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

		// Send JSON response
		String resData = serialize(response);
		OutputStream respBody = exchange.getResponseBody();
		StringStream.writeString(resData, respBody);
		respBody.close();
		exchange.close();
	}

	public static void sendInternalErrorResponse(HttpExchange exchange, Object response) throws IOException {
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

		// Send JSON response
		String failData = serialize(response);
		OutputStream failBody = exchange.getResponseBody();
		StringStream.writeString(failData, failBody);
		failBody.close();
		exchange.close();
	}
}
