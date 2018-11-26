package server.entities;

public class User implements ILocation {

	public static enum UserType {
		DRIVER, BIKER
	}

	private UserType type = null;
	private String token;
	private GpsCoords coords = null;

	public User(String token, UserType type, GpsCoords coords) {
		setToken(token);
		setType(type);
		setLongitude(coords.getLongitude());
		setLatitude(coords.getLatitude());
	}

	public UserType getType() {
		return type;
	}

	public String getToken() {
		return token;
	}
	
	public GpsCoords getCoords() {
		return coords;
	}

	public boolean setType(UserType type) {
		if (type != null) {
			this.type = type;
			return true;
		}
		return false;
	}

	public boolean setToken(String token) {
		if (token != null && !token.isEmpty()) {
			this.token = token;
			return true;
		}
		return false;
	}


	@Override
	public double getLongitude() {
		return getCoords().getLongitude();
	}

	@Override
	public double getLatitude() {
		return getCoords().getLatitude();
	}

	@Override
	public void setLongitude(double longitude) {
		this.coords.setLongitude(longitude);
	}

	@Override
	public void setLatitude(double latitude) {
		this.coords.setLatitude(latitude);
	}

	@Override
	public String toString() {
		return String.format("User type: %s, Coords: %s", this.type, this.coords);
	}
}
