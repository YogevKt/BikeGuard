package server.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
@Entity
public class Area extends Location{
	@JoinTable(name="area_intersection")
	@OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL,targetEntity=Intersection.class)
	private List<Intersection> intersections = null;
	
	public Area() {
		intersections = new ArrayList<>();
	}
	
	public Area(double latitude, double longitude,double altitude) {
		setLongitude(longitude);
		setLatitude(latitude);
		setAltitude(altitude);
		intersections = new ArrayList<>();
	}

	
	public List<Intersection> getIntersections() {
		return intersections;
	}

	public void setIntersections(ArrayList<Intersection> intersections) {
		this.intersections = intersections;
	}
	
	public boolean addIntersection(Intersection intersection) {
		if(intersection != null) {
			this.intersections.add(intersection);
			return true;
		}
		return false;
	}

	public boolean addIntersection(Collection<Intersection> intersections) {
		if(intersections != null && !intersections.isEmpty()) {
			this.intersections.addAll(intersections);
			return true;
		}	
		return false;
	}
	
	public User removeUser(User user) {
		for (Intersection intersection : intersections) {
			return intersection.removeUser(user);
		}
		
		return null;
	}

	
	
	
	
}
