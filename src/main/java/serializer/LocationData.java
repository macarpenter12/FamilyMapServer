package serializer;

public class LocationData {
	private Location[] data;

	public class Location {
		private String country;
		private String city;
		private double latitude;
		private double longitude;

		public Location(String country, String city, double latitude, double longitude) {
			this.country = country;
			this.city = city;
			this.latitude = latitude;
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
	}

	public Location[] getData() {
		return data;
	}

	public void setData(Location[] data) {
		this.data = data;
	}
}
