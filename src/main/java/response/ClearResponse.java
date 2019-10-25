package response;

public class ClearResponse extends Response {
    public ClearResponse() {
        super();
    }

    public ClearResponse(String message, Boolean success) {
        super(message, success);
    }
}
