package Response;

public abstract class Response {
	String message;

	public Response() {
		this.message = "message";
	}

	public Response(String message, String success) {
		this.message = message;
	}
}
