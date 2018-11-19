package server.businessLogic;

import java.util.ArrayList;

import server.entities.Area;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;

public class ServerFacade {
	
	private static ServerFacade serverFacade = null;
	private ArrayList<Area> areas = null;
	
	private ServerFacade() {
		areas = new ArrayList<>();
	}
	
	public static ServerFacade getInstance(){
		if(serverFacade == null)
			serverFacade = new ServerFacade();
		return serverFacade;
	}

	
	
	/***
	 * Returns Collection with all the intersection from each area.
	 * 
	 * @param None
	 *  
	 * @return Array<Intersection>
	 * 
	 * @exception None
	 */
	public ArrayList<Intersection> getIntersections(){
		ArrayList<Intersection> allIntersections = new ArrayList<>();
		
		for (Area area : areas) {
			allIntersections.addAll(area.getIntersections());
		}
		return allIntersections;
	}

	/***
	 * Get all areas
	 * 
	 * @param None
	 *  
	 * @return ArrayList<Intersection>
	 * 
	 * @exception None
	 */
	public ArrayList<Area> getAreas() {
		return areas;
	}

	
	/***
	 * Add user to the users Collection. In case the User already exists update the data.
	 * 
	 * @param User user
	 *  
	 * @return Success criteria (Created/Updated) - Type String.
	 * 
	 * @exception
	 */	
	public String setUser(User user) throws Exception {
		if(user.getToken() !=null && !user.getToken().isEmpty()) {
			
			
			return "sss";
		}else {
			throw new Exception("token is empty or null.");
		}
	}
	
	
}
