package server.businessLogic;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.jni.Thread;

import server.businessLogic.UserAlertsService.Alert;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;


public class ServerServices implements Runnable{
	
	
	
	@Override
	public void run() {
		

		// init intersections from DB
		ServerFacade.getInstance().loadIntersectionFromDB();
		
		//init areas by intersection area
		
		
		//start collision monitor thread
		/*Executors.newSingleThreadExecutor().execute(new Runnable() {
		    @Override
		    public void run() {
		        //TODO collision monitor function
		    	collisionMonitor();
		    }
		});*/
		
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleWithFixedDelay(new Runnable() {
		  @Override
		  public void run() {
			  collisionMonitor();
		  }
		}, 0, 2, TimeUnit.SECONDS);
	
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
		
		ServerFacade.getInstance().getIntersections().parallelStream().forEach(intersection -> checkCollision(intersection));
		
		//each thread check if in the current intersection might be collision
		
		//in case of probability for collision send notification to the user
		
	}
	
	
	private void checkCollision(Intersection intersection) {
		if(intersection != null) {
			Map<String, User> drivers = intersection.getDrivers();
			if(drivers != null) {
				drivers.values().parallelStream().forEach(driver -> alertCollision(driver,intersection.getBikers()));
			}
		}
	}
	
	private void alertCollision(User driver , Map<String, User> bikers) {
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
				sendCollisionAlert(driver, closestBiker, minDistance);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	


}
