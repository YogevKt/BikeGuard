package server.entities;

public abstract class Location implements ILocation{
	
	private double longitude;
	private double latitude;
	
	
	@Override
	public double getLongitude() {
		return longitude;
	}
	@Override
	public double getLatitude() {
		return latitude; 
	}

	@Override
	public void setLongitude(double longitude) {
		this.longitude = longitude;	
	}

	@Override
	public void setLatitude(double latitude) {
		this.latitude = latitude;	
	}
	
	
	@Override
	public String toString() {
		return String.format("[%lf,%lf]", this.longitude, this.latitude);
	}
}
