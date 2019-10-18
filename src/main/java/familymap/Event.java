package familymap;

public class Event {
    private String eventID;
    private String username;
    private String personID;
    private double latitude;
    private double longitude;
    private String country;
    private String city;
    private String type;
    private int year;

    public Event(String eventID, String username, String personID, double latitude, double longitude, String country, String city, String type, int year) {
        this.eventID = eventID;
        this.username = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.type = type;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return (Math.abs(event.latitude - this.latitude) < 0.01) &&
                (Math.abs(event.longitude - this.longitude) < 0.01) &&
                year == event.year &&
                eventID.equals(event.eventID) &&
                username.equals(event.username) &&
                personID.equals(event.personID) &&
                country.equals(event.country) &&
                city.equals(event.city) &&
                type.equals(event.type);
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
