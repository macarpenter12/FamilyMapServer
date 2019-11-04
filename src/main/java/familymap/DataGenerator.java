package familymap;

import com.google.gson.Gson;
import serializer.LocationData;
import serializer.NameData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;

public class DataGenerator {

	/**
	 * Generates a set of a father and mother for a specified person.
	 *
	 * @param person The person for which to generate parents.
	 * @return Array of two Persons, father at index 0 and mother at index 1.
	 */
	public static Person[] generateParents(Person person) {
		try {
			String userName = person.getAssociatedUsername();
			String fatherID = UUID.randomUUID().toString();
			String motherID = UUID.randomUUID().toString();

			// Generate father data
			String fatherFirstName = generateFirstName("m");
			String fatherLastName = person.getLastName();

			// Generate mother data
			String motherFirstName = generateFirstName("f");
			String motherLastName = generateLastName();

			// Put parents into array to return
			Person father = new Person(fatherID, userName, fatherFirstName, fatherLastName,
					"m", null, null, motherID);
			Person mother = new Person(motherID, userName, motherFirstName, motherLastName,
					"f", null, null, fatherID);
			Person[] parents = new Person[2];
			parents[0] = father;
			parents[1] = mother;

			return parents;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates a random male or female first name from JSON file.
	 *
	 * @param gender The desired gender, "m" for male, "f" for female.
	 * @return Randomly generated name.
	 */
	private static String generateFirstName(String gender) throws FileNotFoundException {
		File file = new File("json/" + gender + "names.json");
		// Read JSON data from female name file
		FileReader fr = new FileReader(file);
		NameData fNames = (new Gson()).fromJson(fr, NameData.class);

		// Randomly select name and return
		int random = (int) (Math.random() * fNames.getData().length);
		return fNames.getData()[random];
	}

	/**
	 * Generates a surname for a person from a JSON file.
	 *
	 * @return Randomly generated surname.
	 * @throws FileNotFoundException If JSON file is not found.
	 */
	private static String generateLastName() throws FileNotFoundException {
		File file = new File("json/snames.json");
		// Read JSON data from female name file
		FileReader fr = new FileReader(file);
		NameData names = (new Gson()).fromJson(fr, NameData.class);

		// Randomly select name and return
		int random = (int) (Math.random() * names.getData().length);
		return names.getData()[random];
	}

	private static LocationData.Location generateInitialLocation() throws FileNotFoundException {
		File file = new File("json/locations.json");
		FileReader fr = new FileReader(file);
		LocationData locations = (new Gson()).fromJson(fr, LocationData.class);

		// Randomly select location and return
		int random = (int) (Math.random() * locations.getData().length);
		return locations.getData()[random];
	}

	private static LocationData.Location generateNearbyLocation(String country) throws FileNotFoundException {
		File file = new File("json/locations.json");
		FileReader fr = new FileReader(file);
		LocationData locations = (new Gson()).fromJson(fr, LocationData.class);

		// Get a list of nearby locations
		ArrayList<LocationData.Location> nearbyLocations = new ArrayList<>();
		for (int i = 0; i < locations.getData().length; ++i) {
			if (locations.getData()[i].getCountry().equals(country)) {
				nearbyLocations.add(locations.getData()[i]);
			}
		}

		// Randomly select nearby location and return
		int random = (int) (Math.random() * nearbyLocations.size());
		return nearbyLocations.get(random);
	}

	/**
	 * Generates random Events for a person. Primarily used for the user's Person object, as it does not
	 * depend on events of other associated persons.
	 *
	 * @param person The person for whom to generate events.
	 */
	public static ArrayList<Event> generateEvents(Person person) {
		try {
			ArrayList<Event> events = new ArrayList<>();
			String personID = person.getPersonID();
			String userName = person.getAssociatedUsername();

			// Generate birth
			LocationData.Location location = generateInitialLocation();
			String city = location.getCity();
			String country = location.getCountry();
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			int year = (int) ((Math.random() * (2000 - 1995)) + 1995);
			String eventID = UUID.randomUUID().toString();

			events.add(new Event("birth", personID, city, country, latitude, longitude,
					year, eventID, userName));

			return events;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Event> generateParentEvents(ArrayList<Event> events,
														Person person, Person father, Person mother)
			throws FileNotFoundException {
		// Get birth event for the person
		String personID = person.getPersonID();
		Event personBirth = events.get(0);
		for (Event event : events) {
			if ((event.getPersonID()).equals(personID) &&
					(event.getType()).equals("birth")) {
				personBirth = event;
			}
		}

		String userName = person.getAssociatedUsername();
		String personCountry = personBirth.getCountry();

		// Generate Father's events
		String fatherID = father.getPersonID();
		LocationData.Location location = generateNearbyLocation(personCountry);
		// Birth
		String city = location.getCity();
		String country = location.getCountry();
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		int birthYear = (int) (personBirth.getYear() - (Math.random() * 54) - 16);
		String eventID = UUID.randomUUID().toString();

		events.add(new Event("birth", fatherID, city, country, latitude, longitude,
				birthYear, eventID, userName));

		location = generateNearbyLocation(location.getCountry());
		// Generate mother's birth year now so we can calculate marriage date. Save for later.
		int motherBirthYear = (int) (personBirth.getYear() - (Math.random() * 36) - 14);
		// Marriage
		city = location.getCity();
		country = location.getCountry();
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		int year;
		// If father was born first, make sure mother is old enough for marriage
		if (birthYear < motherBirthYear) {
			year = (int) (motherBirthYear + (Math.random() * 17) + 18);
		}
		else {
			year = (int) (birthYear + (Math.random() * 17) + 18);
		}
		eventID = UUID.randomUUID().toString();

		// Save marriage event for mother
		Event parentsMarriage = new Event("marriage", fatherID, city, country,
				latitude, longitude, year, eventID, userName);
		events.add(parentsMarriage);

		location = generateNearbyLocation(location.getCountry());
		// Death
		city = location.getCity();
		country = location.getCountry();
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		year = (int) (year + (Math.random() * (119 - (year - birthYear))) + 1);
		eventID = UUID.randomUUID().toString();

		events.add(new Event("death", fatherID, city, country, latitude, longitude,
				year, eventID, userName));

		// Generate mother's events
		String motherID = mother.getPersonID();
		location = generateNearbyLocation(location.getCountry());
		// Birth
		city = location.getCity();
		country = location.getCountry();
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		eventID = UUID.randomUUID().toString();

		events.add(new Event("birth", motherID, city, country, latitude, longitude,
				motherBirthYear, eventID, userName));

		// Marriage - deep copy father's event, but change personID
		events.add(new Event("marriage", motherID, parentsMarriage.getCity(), parentsMarriage.getCountry(),
				parentsMarriage.getLatitude(), parentsMarriage.getLongitude(), parentsMarriage.getYear(),
				UUID.randomUUID().toString(), userName));

		location = generateNearbyLocation(location.getCountry());
		// Death
		city = location.getCity();
		country = location.getCountry();
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		year = (int) (year + (Math.random() * (119 - (year - motherBirthYear))) + 1);
		eventID = UUID.randomUUID().toString();

		events.add(new Event("death", motherID, city, country, latitude, longitude,
				year, eventID, userName));

		return events;
	}
}
