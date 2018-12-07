package server.businessLogic;

import java.util.ArrayList;

import server.businessLogic.UserAlertsService.Alert;
import server.entities.Area;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;

public class ServerFacade implements IServerFacade{
	
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
	@Override
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
	 * Check if user entered to dangerous intersection area.
	 *  
	 * @param User user
	 *  
	 * @return Success criteria (Created/Updated) - Type String.
	 * 
	 * @exception
	 */	
	@Override
	public String setUser(User user) throws Exception {
		if(user.getToken() != null && !user.getToken().isEmpty()) {
			
			//start detection of entrance to dangerous intersection
			return detectEntranceToIntersection(user);
		}else {
			throw new Exception("token is empty or null.");
		}
	}
		
	
	/***
	 * Load Intersection data from DB
	 * 
	 * @param None
	 *  
	 * @return Success criteria (Created/Updated) - Type String.
	 * 
	 * @exception
	 */	
	@Override
	public void loadIntersectionFromDB() {
		
	}
	
	/*** 
	 * Detection of entrance to dangerous intersection
	 * 
	 * @param User
	 * 
	 * @throws Exception 
	 */
	private String detectEntranceToIntersection(User user) throws Exception {
		final String TITLE = "Dangerous Intersection";
		final String BODY = "dangerous intersection ahead";
		ArrayList<Intersection> intersections = ServerFacade.getInstance().getIntersections();
		
		if(intersections == null)
			throw new Exception("Cant detect entrance because intersection null or havn't loaded properly.");
		
		for (Intersection intersection : intersections) {
			try {
				double distance = Location.distance(intersection, user.getCoords());

				if( distance <= Location.INTERSECTION_NOTIFICATION_DISTANCE) {
					switch (user.getType()) {
					case BIKER:
						intersection.addBiker(user);
						break;
					case DRIVER:
						intersection.addDriver(user);
						break;
					default:
						throw new Exception("Couldn't recognize user type.");
					}
					
					//Send notification
					if (!UserAlertsService.getInstance().isUserAlerted(user.getToken(), Alert.INTERSECTION_ENTRANCE)) {
						return FireBaseServiceHandler.sendPushNotification(user,TITLE,BODY);
					}
					
				}else if(distance > Location.INTERSECTION_NOTIFICATION_DISTANCE) { //can be less than X
					if(intersection.removeUser(user) != null)
						UserAlertsService.getInstance().removeUserAlert(user.getToken());
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return "Error";
			}
		}
		
		return "Error";
	}
	
	
	/*** 
	 * Add new area
	 * 
	 * @param Area
	 * 
	 * @throws Exception 
	 */
	@Override
	public void addArea(Area area) throws Exception {
		if(area !=null) {
			this.areas.add(area);
		}else {
			throw new Exception("The area you tried to had is null");			
		}
	}
	
	/*** 
	 * Add new collection of areas
	 * 
	 * @param ArrayList<Area>
	 * 
	 * @throws Exception 
	 */
	@Override
	public void addArea(ArrayList<Area> areas) throws Exception {
		if(areas !=null && !areas.isEmpty()) {
			this.areas.addAll(areas);
		}else {
			throw new Exception("The areas you tried to had is null or empty");			
		}
	}
	

	/*** 
	 * Add new intersection
	 * 
	 * @param Intersection
	 * 
	 * @throws Exception 
	 */
	@Override
	public void addIntersection(Intersection intersection) throws Exception {
		if(intersection !=null) {
			for (Area area : areas) {
				if(Location.distance(area, intersection) < Location.AREA_DISTANCE_RADIUS) {
					area.addIntersection(intersection);
				}
			}
		}else {			
			throw new Exception("The intersection you tried to had is null");
		}
	}
	
	/*** 
	 * Add new collection of intersections to area
	 * 
	 * @param ArrayList<Intersection>
	 * 
	 * @throws Exception 
	 */
	@Override
	public void addIntersection(Area area, ArrayList<Intersection> intersections) throws Exception {
		if(area !=null && intersections != null && !intersections.isEmpty()) {
			for (Intersection intersection : intersections) {
				if(Location.distance(area, intersection) < Location.AREA_DISTANCE_RADIUS) {
					area.addIntersection(intersection);
				}
			}
		}else {
			throw new Exception("The intersections you tried to had is null or empty");			
		}
	}
	
	
	
}