package service;

import familymap.Person;

/**
 * Returns the Person associated with the given personID, along with all of that Person's
 * family members. Will help return json data.
 */
public class PersonService {
    private String AssociatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private Person[] familyMembers;

    /**
     * @param request The json object to process.
     * @return Returns the response as a json object.
     */
    public String find(String request) {
        return null;
    }
}
