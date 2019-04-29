package server.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

public class Area extends Location{
	private ArrayList<Intersection> intersections = null;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Area(double latitude, double longitude) {
		setLongitude(longitude);
		setLatitude(latitude);
		intersections = new ArrayList<>();
	}

	@OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL)
	public ArrayList<Intersection> getIntersections() {
		return intersections;
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
