package server.entities;

import java.util.HashMap;
import java.util.Map;

public class Intersection extends Location{
	
	private Map<String, User> bikers = null;
	private Map<String, User> drivers = null;

	public Intersection(double longitude, double latitude) {
		setLongitude(longitude);
		setLatitude(latitude);
		
		bikers = new HashMap<>();
		drivers = new HashMap<>();
		
	}

	public Map<String, User> getBikers() {
		return bikers;
	}

	public Map<String, User> getDrivers() {
		return drivers;
	}


	public boolean addBiker(User biker) {
		if(biker != null && !biker.getToken().isEmpty()) {
			this.bikers.put(biker.getToken(), biker);
			return true;
		}
		
		return false;
	}
	
	
	public boolean addDriver(User driver) {
		if(driver != null && !driver.getToken().isEmpty()) {
			this.drivers.put(driver.getToken(), driver);
			return true;
		}
		
		return false;
	}
	
	public User removeUser(User user) {
		
		if(user != null && !user.getToken().isEmpty()) {
			
			switch(user.getType()) {
				case BIKER:
					return bikers.remove(user.getToken());
				case DRIVER:
					return drivers.remove(user.getToken());
			}
		}
		
		return null;
	}
	
	
}
