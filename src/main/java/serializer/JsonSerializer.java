package serializer;

import com.google.gson.Gson;
import familymap.Person;
import server.AuthToken;

public class JsonSerializer {

	public static <T> T deserialize(String value, Class<T> returnType) {
		return (new Gson()).fromJson(value, returnType);
	}

	public static String serialize(Object obj) {
		return (new Gson()).toJson(obj);
	}
}
