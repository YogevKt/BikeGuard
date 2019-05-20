package server.businessLogic;

import java.util.ArrayList;
import java.util.List;

import server.entities.Area;
import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.User;

public interface IServerFacade {
	
	/*** 
	 * Functionality: 
	 * 
	 * Calculate distance
	 * Alert in entrance to dangerous intersection
	 * Alert in case of probability to collision
	 * Update User data
	 * Get Area information
	 * Get Intersection data
	 * 
	 * 
	 * */
	List<Intersection> getIntersections();
	String setUser(User user) throws Exception;
	void addArea(Area area) throws Exception;
	void addArea(ArrayList<Area> areas) throws Exception;
	void addIntersection(Intersection intersection) throws Exception;
	void addIntersection(Area area, ArrayList<Intersection> intersections) throws Exception;
	public ArrayList<GpsCoords> getIntersectionsCoords();
	public User removeUserFromIntersection(User user);

	
	

}
