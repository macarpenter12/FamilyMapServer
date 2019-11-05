package response;

public class DataResponse extends Response {
	private Object[] data;

	public DataResponse(Object[] data) {
		this.data = data;
	}

	public DataResponse(String message, boolean success) {
		super(message, success);
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}
}
