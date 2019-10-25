package response;

public abstract class Response {
	String message;
	Boolean success;

	public Response() {
		this.message = "message";
		this.success = false;
	}

	public Response(String message, Boolean success) {
		this.message = message;
		this.success = success;
	}
}
