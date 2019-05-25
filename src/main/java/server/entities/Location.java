package server.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import server.businessLogic.CartesianCoord;
import server.businessLogic.RestService;

@MappedSuperclass
public abstract class Location implements ILocation{
	@Transient
	public static final int INTERSECTION_NOTIFICATION_DISTANCE = 300;
	@Transient
	public static final int AREA_DISTANCE_RADIUS = 10000;
	@Transient
	public static final double HIGH_ALERT_DISTANCE = 35;
	@Transient
	public static final double MEDIUM_ALERT_DISTANCE = 75;
	@Transient
	public static final double LOW_ALERT_DISTANCE = 100;
	@Transient
	public static final double COLLISION_TIME_DELTA = 5;
	
	private double longitude;
	private double latitude;
	private double altitude;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public double getLatitude() {
		return latitude; 
	}

	@Override
	public void setLongitude(double longitude) {
		this.longitude = longitude;	
	}

	@Override
	public void setLatitude(double latitude) {
		this.latitude = latitude;	
	}
	
	@Override
	public double getAltitude() {
		return altitude;
	}
	
	@Override
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	/***
	 * Calculate distance between Location objects.
	 * 
	 *  
	 * @return double
	 * 
	 * @exception Exception
	 */
	public static double distance(Location coordA, Location coordB) throws Exception {
		if(coordA != null && coordB != null) {
			return distance(coordA.getLatitude(), coordA.getLongitude(), coordB.getLatitude(), coordB.getLongitude());
			//return res/1000.0;
		}else {
			throw new Exception("Coord A or B was null");
		}
	}
	
	
	
	private static double distance(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	}


	public static CartesianCoord calculateIntersectionPoint(User driver, User biker) {
		RestService restService = new RestService();
		CartesianCoord intersectionPoint = new CartesianCoord();

		//convert gps coords to cartesian
		CartesianCoord driverCurrentCoords = restService.convertGpsCoordsToCartesianCoord(driver.getCoords());
		CartesianCoord driverPreviousCoords = restService.convertGpsCoordsToCartesianCoord(driver.getPreviousCoords());
		
		CartesianCoord bikerCurrentCoords = restService.convertGpsCoordsToCartesianCoord(biker.getCoords());
		CartesianCoord bikerPreviousCoords = restService.convertGpsCoordsToCartesianCoord(biker.getPreviousCoords());
		
		//find the linear equation for driver
		double mDriver = ( driverCurrentCoords.getY() - driverPreviousCoords.getY() ) / 
				( driverCurrentCoords.getX() - driverPreviousCoords.getX() ); 
		
		
		//find the linear equation for biker
		double mBiker = ( bikerCurrentCoords.getY() - bikerPreviousCoords.getY() ) / 
				( bikerCurrentCoords.getX() - bikerPreviousCoords.getX() ); 
		
		//find intersection point between the lines
		double b = -mDriver*driverCurrentCoords.getX() + driverCurrentCoords.getY();
		double d = -mBiker*bikerCurrentCoords.getX() + bikerCurrentCoords.getY();
		
		intersectionPoint.setX( (d-b)/(mDriver-mBiker) );
		intersectionPoint.setY(mDriver*intersectionPoint.getX() + b);

		intersectionPoint.setZ((driverCurrentCoords.getZ()+bikerCurrentCoords.getZ())/2.0);
		intersectionPoint.setCoords(restService.convertCartesianToGpsCoords(intersectionPoint));
		return intersectionPoint;
	}
	
	@Override
	public String toString() {
		return String.format("[%f,%f,%f]", this.latitude, this.longitude, this.altitude);
	}
}
