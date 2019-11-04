package response;

public class LoadResponse extends Response {

	public LoadResponse(int numUsers, int numPersons, int numEvents, Boolean success) {
		this.success = success;
		if (success == true) {
			this.message = "Successfully added " +
					numUsers + " users, " +
					numPersons + " persons, and " +
					numEvents + " events to the database.";
		}
		else {
			this.message = "Something went wrong.";
		}
	}

	public LoadResponse(String message, Boolean success) {
		super(message, success);
	}
}
