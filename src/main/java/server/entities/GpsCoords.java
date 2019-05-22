package server.entities;

public class GpsCoords extends Location{

	public GpsCoords(double latitude, double longitude, double altitude) {
		setLongitude(longitude);
		setLatitude(latitude);
		setAltitude(altitude);
	}
	
	
}
