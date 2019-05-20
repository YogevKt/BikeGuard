package server.businessLogic;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.businessLogic.UserAlertsService.Alert;
import server.dal.AreaDao;
import server.entities.Area;
import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;

@Service
public class ServerFacade implements IServerFacade{

	private List<Intersection> intersections = null;
	private List<Area> areas = null;
	private AreaDao areasDao;
	
	private ServerFacade() {
		areas = new ArrayList<>();
	}
	
	@Autowired
	private ServerFacade(AreaDao areasDao) {
		areas = new ArrayList<>();
		this.areasDao=areasDao;
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
	public List<Intersection> getIntersections(){
		if(intersections == null) {
			intersections = new ArrayList<Intersection>();

			for (Area area : areas) {
				intersections.addAll(area.getIntersections());
			}
		}

		return intersections;
	}
	
	/***
	 * Returns Collection with all the intersection Coords from each area.
	 * 
	 * @param None
	 *  
	 * @return Array<Intersection>
	 * 
	 * @exception None
	 */
	@Override
	public ArrayList<GpsCoords> getIntersectionsCoords(){
		ArrayList<GpsCoords> allIntersectionsCoords = new ArrayList<>();
		
		for (Area area : areas) {
			for(Intersection intersection : area.getIntersections()) {
				allIntersectionsCoords.add(new GpsCoords(intersection.getLatitude(), intersection.getLongitude()));
			}
		}

		return allIntersectionsCoords;
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
	public List<Area> getAreas() {
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
	 * Init server on start-up
	 * 
	 * @param None
	 *  
	 * @return Success criteria (Created/Updated) - Type String.
	 * 
	 * @exception
	 */	
	@PostConstruct
	@Autowired
	private void initializeServer() {
		System.err.print("Start initializing server");
		System.err.print("Load data from DB");
		loadIntersectionFromDB();
		System.err.println("Start server services monitor");
		new Thread(new ServerServices(this)).start();
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
	private void loadIntersectionFromDB() {
		Iterable<Area> areasIterator = areasDao.findAll();
		for (Area area : areasIterator) {
			areas.add(area);
		}
		
		intersections = getIntersections();
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
		
		return "Success";
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
					intersections.add(intersection);
					areasDao.save(area);
					break;
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
	
	/*** 
	 * Remove user
	 * 
	 * @param ArrayList<Intersection>
	 * 
	 * @throws Exception 
	 */
	@Override
	public User removeUserFromIntersection(User user) {
		for (Area area : areas) {
			return area.removeUser(user);
		}
		return null;
	}
	
	
	
}