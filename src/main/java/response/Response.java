package response;

public class Response {
  String message;
  Boolean success;

  public Response() {
	this.message=null;
	this.success=null;
  }

  public Response(String message, Boolean success) {
	this.message=message;
	this.success=success;
  }

  public String getMessage() {
	return message;
  }

  public Boolean getSuccess() {
	return success;
  }
}
