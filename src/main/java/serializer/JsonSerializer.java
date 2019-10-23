package serializer;

import com.google.gson.Gson;

public class JsonSerializer {
	public static <T> T deserialize(String value, Class<T> returnType) {
		return (new Gson()).fromJson(value, returnType);
	}
}
