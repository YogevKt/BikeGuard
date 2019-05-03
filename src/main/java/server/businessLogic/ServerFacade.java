package server.businessLogic;

import java.util.ArrayList;
import java.util.List;

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
		debug();
	}
	
	public void debug() {
		ServerFacade server = this;
		
		try {
		
			//add area
			Area area1 = new Area(31.96828999958059,34.80140645543159);
			server.addArea(area1);//area may
			Area area2 = new Area(32.268066,34.911650);
			server.addArea(area2);//area yogev
			
			//add intersections	nearby may's home		
			server.addIntersection(new Intersection(31.968111,34.800483)); //76~ from may's home
			server.addIntersection(new Intersection(31.968210,34.800753)); //46~ from may's home
			server.addIntersection(new Intersection(31.96828999958059,34.80140645543159)); //17~ from may's home
			
			//add intersection nearby yogev's home
			server.addIntersection(new Intersection(32.268066, 34.911650));//95~ from yogev's home
			server.addIntersection(new Intersection(31.267475, 34.911670));//44~ from yogev's home
		
			areasDao.save(area1);
			areasDao.save(area2);
			
			//add driver
			/*server.setUser(new User("clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
					User.UserType.DRIVER, new GpsCoords(31.968210,34.800753)));*/
			
			
			/*server.setUser(new User("fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
					User.UserType.BIKER, new GpsCoords(32.267659,34.911553)));*/
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		Iterable<Area> areasIterator = areasDao.findAll();
		for (Area area : areasIterator) {
			areas.add(area);
		}
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