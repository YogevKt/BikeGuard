package server.businessLogic;


import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import server.businessLogic.UserAlertsService.Alert;
import server.entities.Intersection;
import server.entities.Location;
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
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleWithFixedDelay(new Runnable() {
		  @Override
		  public void run() {
			  collisionMonitor();
		  }
		}, 0, 1, TimeUnit.SECONDS);
	}
	


	/*** 
	 * Running monitor that sample every 1 seconds if user might coiled inside intersection area.
	 * 
	 * Steps:
	 * 1.run over each intersection
	 * 2.for each intersection check collision for each driver
	 * 3.find users driving vector
	 * 4.find users collision point
	 * 5.check if users ahead to collision point
	 * 6.calculate the difference at arrive time of the users to the collision point
	 * 7. alert in case the difference at arrive time less then the final set at settings.
	 * 
	 * @param None
	 * 
	 * 
	 */
	private void collisionMonitor() {
		//Create stream contains task for each intersection
		//each thread check if in the current intersection might be collision		
		//in case of probability for collision send notification to the user
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
				sendCollisionAlert(driver,Location.distance(driver.getCoords(), intersectionPoint.getGPSCoords()));
				sendCollisionAlert(biker,Location.distance(biker.getCoords(), intersectionPoint.getGPSCoords()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	private boolean isHeadingToIntersectionPoint(User user, CartesianCoord intersectionPoint) {

		try {
			if(Location.distance(user.getCoords(), intersectionPoint.getGPSCoords())
					<= Location.distance(user.getPreviousCoords(), intersectionPoint.getGPSCoords()))
				return true;

		} catch (Exception e) {
			System.err.println("Could not calc user direction.");
		}

		return false;
	}
	
	private double timeOfArrivalToIntersectionPoint(User user, CartesianCoord intersectionPoint) {
		try {
			return Location.distance(user.getCoords(), intersectionPoint.getGPSCoords()) / user.getSpeed();
		} catch (Exception e) {
			return -1;
		}
	}
	
	private void sendCollisionAlert(User user, double distance) {
		boolean isAlerted;
		String alertOn;
		
		if(user.getType() == User.UserType.BIKER){
			alertOn="driver";
		}else{
			alertOn="biker";
		}
		
		if(distance <= Location.LOW_ALERT_DISTANCE && distance > Location.MEDIUM_ALERT_DISTANCE){
			//low alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(user.getToken(), Alert.LOW);
			
			if(!isAlerted)	{
				FireBaseServiceHandler.sendPushNotification(user,"Low Alert","In "+(int)distance+" there's a "+alertOn);
			}
		}else if(distance <= Location.MEDIUM_ALERT_DISTANCE && distance > Location.HIGH_ALERT_DISTANCE) {
			//medium alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(user.getToken(), Alert.MEDIUM);
			
			if(!isAlerted)	{
				FireBaseServiceHandler.sendPushNotification(user,"Medium Alert","In "+(int)distance+" there's a "+alertOn);
			}
			
		}else if(distance <= Location.HIGH_ALERT_DISTANCE) {
			//high alert
			isAlerted = UserAlertsService.getInstance().isUserAlerted(user.getToken(), Alert.HIGH);
			
			if(!isAlerted) {
				FireBaseServiceHandler.sendPushNotification(user,"High Alert","In "+(int)distance+"m there's a "+alertOn);
			}
		}
	}
	

}