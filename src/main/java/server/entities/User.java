package server.entities;

public class User implements ILocation {

	public static enum UserType {
		DRIVER, BIKER
	}

	private UserType type = null;
	private String token;
	private GpsCoords currentCoords = null;
	private GpsCoords previousCoords = null;
	private float speed = 0;

	public User(String token, UserType type, GpsCoords coords) {
		setToken(token);
		setType(type);
		this.currentCoords = new GpsCoords(coords.getLatitude(),coords.getLongitude());
	}

	public UserType getType() {
		return type;
	}

	public String getToken() {
		return token;
	}
	
	public GpsCoords getCoords() {
		return currentCoords;
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
		this.currentCoords.setLongitude(longitude);
	}

	@Override
	public void setLatitude(double latitude) {
		this.currentCoords.setLatitude(latitude);
	}
	
	public void setCoords(GpsCoords coords) {
		this.previousCoords = this.currentCoords;
		this.currentCoords = new GpsCoords(coords.getLatitude(), coords.getLongitude());
	}
	
	

	public GpsCoords getPreviousCoords() {
		return previousCoords;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		if(speed >= 0)
			this.speed = speed;
	}

	@Override
	public String toString() {
		return String.format("User type: %s, Coords: %s", this.type, this.currentCoords);
	}
}
