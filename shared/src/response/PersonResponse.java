package response;

import familymap.Person;

public class PersonResponse extends Response {
  private Person[] data;

  public PersonResponse(Person[] data) {
	this.data=data;
  }

  public PersonResponse(String message, boolean success) {
	super(message, success);
  }

  public Person[] getData() {
	return data;
  }

  public void setData(Person[] data) {
	this.data=data;
  }

}
