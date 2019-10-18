package service;

import familymap.Event;

/**
 * Returns the Event associated with the given eventID, along with ALL Events belonging to the
 * current user AND family members of the user. Will help return json data.
 */
public class EventService {
    private String associatedUsername;
    private String eventID;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;
    private Event[] familyEvents;

    /**
     * @param request The json object to process.
     * @return Returns the response as a json object.
     */
    public String find(String request) {
        return null;
    }
}
