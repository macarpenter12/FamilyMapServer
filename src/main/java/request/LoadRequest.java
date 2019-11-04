package request;

import familymap.Event;
import familymap.Person;
import familymap.User;

public class LoadRequest extends Request {
	private User[] users;
	private Person[] persons;
	private Event[] events;

	public User[] getUsers() {
		return users;
	}

	public Person[] getPersons() {
		return persons;
	}

	public Event[] getEvents() {
		return events;
	}

	public LoadRequest() {
		super();
	}

	public LoadRequest(User[] users, Person[] persons, Event[] events) {
		this.users = users;
		this.persons = persons;
		this.events = events;
	}
}
