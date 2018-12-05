package server.businessLogic;


import java.util.ArrayList;
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
		System.err.println(">>>  Start Server Services Thread  <<<");

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
		System.err.println(">>>  Collision Monitor Thread  <<<");
		//Create stream contains task for each intersection
		
		ServerFacade.getInstance().getIntersections().parallelStream().forEach(intersection -> checkCollision(intersection));
		
		//each thread check if in the current intersection might be collision
		
		//in case of probability for collision send notification to the user
		
	}
	
	
	private void checkCollision(Intersection intersection) {
		if(intersection != null) {
			ArrayList<User> drivers = intersection.getDrivers();
			if(drivers != null) {
				drivers.parallelStream().forEach(driver -> alertCollision(driver,intersection.getBikers()));
			}
		}
	}
	
	private void alertCollision(User driver , ArrayList<User> bikers) {
		double minimumDistance;
		
		try {
			for (User biker : bikers) {
				double distance = Location.distance(driver.getCoords(), biker.getCoords());
				boolean isAlerted;

				if(distance <=75 && distance >35) {
					//medium alert
					isAlerted = UserAlertsService.getInstance().isUserAlerted(biker.getToken(), Alert.MEDIUM);
					
					if(!isAlerted)	{
						FireBaseServiceHandler.sendPushNotification(biker,"Medium Alert","In "+(int)distance+" there's a driver");
					}
					
				}else if(distance<=35) {
					//high alert
					isAlerted = UserAlertsService.getInstance().isUserAlerted(biker.getToken(), Alert.HIGH);
					
					if(!isAlerted) {
						FireBaseServiceHandler.sendPushNotification(biker,"High Alert","In "+(int)distance+"m there's a driver");
					}
				}
					
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
