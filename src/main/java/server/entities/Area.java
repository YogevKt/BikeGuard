package server.entities;

import java.util.ArrayList;
import java.util.Collection;

public class Area extends Location{
	
	private ArrayList<Intersection> intersections = null;
	
	public Area(double longitude, double latitude) {
		setLongitude(longitude);
		setLatitude(latitude);
		intersections = new ArrayList<>();
	} 

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
	
	
	
}
