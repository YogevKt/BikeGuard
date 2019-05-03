package server.entities;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

@Entity
public class Intersection extends Location{

	@Transient
	private Map<String, User> bikers = null;
	@Transient
	private Map<String, User> drivers = null;
	
	

	public Intersection(double latitude, double longitude) {
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
			if(bikers.containsKey(biker.getToken())) {
				User localBiker = this.bikers.get(biker.getToken());
				localBiker.setCoords(biker.getCoords());
				localBiker.setSpeed(biker.getSpeed());
			}else {
				this.bikers.put(biker.getToken(), biker);
			}
			return true;
		}
		
		return false;
	}
	
	
	public boolean addDriver(User driver) {
		if(driver != null && !driver.getToken().isEmpty()) {
			if(drivers.containsKey(driver.getToken())) {
				User localDriver = this.drivers.get(driver.getToken());
				localDriver.setCoords(driver.getCoords());
				localDriver.setSpeed(driver.getSpeed());
			}else {
				this.drivers.put(driver.getToken(), driver);
			}
			
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
