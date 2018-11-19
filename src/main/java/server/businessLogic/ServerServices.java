package server.businessLogic;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;

public class ServerServices implements Runnable{
	
	
	
	@Override
	public void run() {
		System.err.println(">>>  Start Server Services Thread  <<<");

		// init intersections from DB
		ServerFacade.getInstance().loadIntersectionFromDB();
		
		//init areas by intersection area
		
		
		//start collision monitor thread
		Executors.newSingleThreadExecutor().execute(new Runnable() {
		    @Override
		    public void run() {
		        //TODO collision monitor function
		    	collisionMonitor();
		    }
		});
		
		//start detection of entrance to dangerous intersection thread
		Executors.newSingleThreadExecutor().execute(new Runnable() {
		    @Override
		    public void run() {
		        //TODO detection of entrance function
		    	//detectEntranceToIntersection();
		    }
		});
		
		
	}
	
	/*** 
	 * 
	 */
	private void collisionMonitor() {
		System.err.println(">> not implimented yet <<");
	}
	
	
	/*** 
	 * Dont know if its should be thread or call by user update function? 
	 * @throws Exception 
	 */
	private void detectEntranceToIntersection(User user) throws Exception {
		ArrayList<Intersection> intersections = ServerFacade.getInstance().getIntersections();
		
		if(intersections == null)
			throw new Exception("Cant detect entrance because intersection null or havn't loaded properly.");
		
		for (Intersection intersection : intersections) {
			try {
				if(Location.distance(intersection, user.getCoords()) <= Location.INTERSECTION_NOTIFICATION_DISTANCE) {
					//Send notification
					FireBaseServiceHandler.sendFireBaseNotification(user);
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
