package server.entities;

import java.util.ArrayList;
import java.util.Collection;

public class Intersection extends Location{
	
	private ArrayList<User> bikers = null;
	private ArrayList<User> drivers = null;

	public Intersection(double longitude, double latitude) {
		setLongitude(longitude);
		setLatitude(latitude);
		
		bikers = new ArrayList<>();
		drivers = new ArrayList<>();
		
	}

	public ArrayList<User> getBikers() {
		return bikers;
	}

	public ArrayList<User> getDrivers() {
		return drivers;
	}


	public boolean addBiker(User biker) {
		if(biker != null) {
			this.bikers.add(biker);
			return true;
		}
		
		return false;
	}
	
	
	public boolean addDriver(User driver) {
		if(driver != null) {
			this.drivers.add(driver);
			return true;
		}
		
		return false;
	}
	
	
}
