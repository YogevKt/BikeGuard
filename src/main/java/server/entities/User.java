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
	}

	public UserType getType() {
		return type;
	}

	public String getToken() {
		return token;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLatitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setLongitude(double longitude) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLatitude(double latitude) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return String.format("User type: %s", this.type);
	}
}
