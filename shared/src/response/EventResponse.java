package response;

import familymap.Event;

public class EventResponse extends Response {
  private Event[] data;

  public EventResponse(Event[] data) {
	this.data=data;
  }

  public EventResponse(String message, boolean success) {
	super(message, success);
  }

  public Event[] getData() {
	return data;
  }

  public void setData(Event[] data) {
	this.data=data;
  }
}
