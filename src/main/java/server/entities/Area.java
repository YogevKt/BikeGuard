package server.entities;

import java.util.ArrayList;

public class Area {
	
	private ArrayList<Intersection> intersections = null;
	
	public Area() {
		intersections = new ArrayList<>();
	} 

	public ArrayList<Intersection> getIntersections() {
		return intersections;
	}

	
	
}
