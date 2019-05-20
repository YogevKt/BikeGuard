package server.businessLogic;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import server.businessLogic.UserAlertsService.Alert;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.Location.CartesianCoord;
import server.entities.User;

@Service
public class ServerServices implements Runnable{
	private ServerFacade serverfacade;
	
	public ServerServices(ServerFacade serverfacade) {
		this.serverfacade=serverfacade;
	}
	
	@Override
	public void run() {
	
		//start collision monitor thread
//		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
//		exec.scheduleWithFixedDelay(new Runnable() {
//		  @Override
//		  public void run() {
//			  collisionMonitor();
//		  }
//		}, 0, 1, TimeUnit.SECONDS);
		debugCollision();
	}
	
	private void debugCollision() {
		List<Intersection> intersections = serverfacade.getIntersections();
		for (Intersection intersection : intersections) {
			if(intersection != null) {
				Map<String, User> drivers = intersection.getDrivers();
				if(drivers != null) {
					intersection.getDrivers().values().forEach(driver->{
						debug2(driver,intersection.getBikers(),intersection);
					});
				}
			}
		}
	}

	private void debug2(User driver, Map<String, User> bikers, Intersection intersection) {
		bikers.values().stream().forEach(biker -> alertUsersOnCollisionUser(driver,biker,intersection));		
	}

	/*** 
	 * Running monitor that sample every X seconds if user might coiled inside intersection area.
	 * 
	 * Steps:
	 * 
	 * 
	 * 
	 * @param None
	 * 
	 * 
	 */
	private void collisionMonitor() {
		//Create stream contains task for each intersection
		//each thread check if in the current intersection might be collision		
		//in case of probability for collision send notification to the user
		System.err.println("collision monitor");
		List<Intersection> intersections = serverfacade.getIntersections();
		intersections.parallelStream().forEach(intersection -> checkCollision(intersection));
		
	}
	
	private void checkCollision(Intersection intersection) {
		if(intersection != null) {
			Map<String, User> drivers = intersection.getDrivers();
			if(drivers != null) {
				drivers.values().parallelStream().forEach(driver -> alertCollision(driver,intersection.getBikers(),intersection));
			}
		}
	}
	
	private void alertCollision(User driver , Map<String, User> bikers ,Intersection intersection) {
		bikers.values().parallelStream().forEach(biker -> alertUsersOnCollisionUser(driver,biker,intersection));	
	}
	
	private void alertUsersOnCollisionUser(User driver, User biker,Intersection intersection) {
		System.err.println("alertUsersOnCollisionUser");
		//convert gps coords to cartesian and find intersection point between the users
		if(driver.getPreviousCoords() == null || biker.getPreviousCoords()==null)
			return;
		
		CartesianCoord intersectionPoint = Location.calculateIntersectionPoint(driver, biker);
		
		//check if the intersection point placed inside the intersection area
		try {
			if(Location.distance(intersection, intersectionPoint.getGPSCoords()) > Location.INTERSECTION_NOTIFICATION_DISTANCE)
				return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		//check if the both users driving ahead to this point
		if(!isHeadingToIntersectionPoint(driver,intersectionPoint) || !isHeadingToIntersectionPoint(biker,intersectionPoint))
			return;
		
		//calculate the the of arrival of each user to the point
		double tDriver = timeOfArrivalToIntersectionPoint(driver,intersectionPoint);
		double tBiker = timeOfArrivalToIntersectionPoint(biker,intersectionPoint);
		//alert user by measurements
		if(Math.abs(tBiker-tDriver) <=  Location.COLLISION_TIME_DELTA) {
			try {
				sendCollisionAlert(driver,biker,Location.distance(driver.getCoords(), biker.getCoords()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	private boolean isHeadingToIntersectionPoint(User user, CartesianCoord intersectionPoint) {
		CartesianCoord userCurrentCoords = new CartesianCoord(user.getCoords());
		CartesianCoord userPreviousCoords = new CartesianCoord(user.getPreviousCoords());
		
		if(intersectionPoint.distance(userCurrentCoords) <= intersectionPoint.distance(userPreviousCoords))
			return true;
		else
			return false;
	}
	
	private double timeOfArrivalToIntersectionPoint(User user, CartesianCoord intersectionPoint) {
		try {
			return Location.distance(user.getCoords(), intersectionPoint.getGPSCoords()) / user.getSpeed();
		} catch (Exception e) {
			return -1;
		}
	}
	
	private void sendCollisionAlert(User driver, User biker, double distance) {
		boolean isAlerted;
		if(distance <= Location.MEDIUM_ALERT_DISTANCE && distance > Location.HIGH_ALERT_DISTANCE) {
			//medium alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(biker.getToken(), Alert.MEDIUM);
			
			if(!isAlerted)	{
				FireBaseServiceHandler.sendPushNotification(biker,"Medium Alert","In "+(int)distance+" there's a driver");
			}
			
		}else if(distance <= Location.HIGH_ALERT_DISTANCE) {
			//high alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(biker.getToken(), Alert.HIGH);
			
			if(!isAlerted) {
				FireBaseServiceHandler.sendPushNotification(biker,"High Alert","In "+(int)distance+"m there's a driver");
			}
		}
	}
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////
 	
	
 	@Deprecated
	private void alertCollisionOLD(User driver , Map<String, User> bikers) {
		double minDistance = Location.MEDIUM_ALERT_DISTANCE;
		User biker;
		User closestBiker = null;

		try {
			Iterator<User> bikersIterator = bikers.values().iterator();
			
			while(bikersIterator.hasNext()) {
				biker = bikersIterator.next();
				double distance = Location.distance(driver.getCoords(), biker.getCoords());
				if(distance <= minDistance) {
					minDistance = distance;
					closestBiker = biker;
				}
			}
			if (closestBiker != null) {
				sendCollisionAlertOLD(driver, closestBiker, minDistance);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Deprecated
 	private void sendCollisionAlertOLD(User driver, User biker, double distance) {
		boolean isAlerted;
		if(distance <= Location.MEDIUM_ALERT_DISTANCE && distance > Location.HIGH_ALERT_DISTANCE) {
			//medium alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(biker.getToken(), Alert.MEDIUM);
			
			if(!isAlerted)	{
				FireBaseServiceHandler.sendPushNotification(biker,"Medium Alert","In "+(int)distance+" there's a driver");
			}
			
		}else if(distance <= Location.HIGH_ALERT_DISTANCE) {
			//high alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(biker.getToken(), Alert.HIGH);
			
			if(!isAlerted) {
				FireBaseServiceHandler.sendPushNotification(biker,"High Alert","In "+(int)distance+"m there's a driver");
			}
		}
	}
	


}