package response;

public class DataResponse extends Response {
	private String[] data;

	public DataResponse(String[] data) {
		super(null, null);
		this.data = data;
	}

	public DataResponse(String message, boolean success) {
		super(message, success);
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}
}
