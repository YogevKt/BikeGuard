package server.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.google.gson.JsonObject;

import server.businessLogic.RestClient;

@MappedSuperclass
public abstract class Location implements ILocation{
	@Transient
	public static final int INTERSECTION_NOTIFICATION_DISTANCE = 100;
	@Transient
	public static final int AREA_DISTANCE_RADIUS = 10000;
	@Transient
	public static final double HIGH_ALERT_DISTANCE = 35;
	@Transient
	public static final double MEDIUM_ALERT_DISTANCE = 75;
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
		CartesianCoord intersectionPoint = new CartesianCoord();
		
		//convert gps coords to cartesian
		CartesianCoord driverCurrentCoords = new CartesianCoord(driver.getCoords());
		CartesianCoord driverPreviousCoords = new CartesianCoord(driver.getPreviousCoords());
		
		CartesianCoord bikerCurrentCoords = new CartesianCoord(biker.getCoords());
		CartesianCoord bikerPreviousCoords = new CartesianCoord(biker.getPreviousCoords());
		
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
		
		return intersectionPoint;
	}
	
	public static CartesianCoord geodetic_to_cartesian(double lat,double lon,double alt) {
		final String URI = "/geodetic_to_cartesian";
		RestClient restClient = new RestClient();
		JsonObject json = new JsonObject();
		
		json.addProperty("Latitude", lat);
		json.addProperty("Longitude", lon);
		json.addProperty("Altitude", alt);
		
		String respones = restClient.post2(URI, json.toString());
		System.out.println(respones);
		return null;
	}
	
	public static GpsCoords cartesian_to_geodetic(double x, double y, double z) {
		return null;
	}
	
	
	public static class CartesianCoord{
		private double x;
		private double y;
		private double z;
		private GpsCoords coords;
		
		private final double R = 6371; // in km
		
		public CartesianCoord() {	
		}
		
		public CartesianCoord(double x, double y, double z) {
			super();
			setX(x);
			setY(y);
			setZ(z);
		}
		
		public CartesianCoord(GpsCoords gpsCoords) {
			super();
			this.coords = gpsCoords;
			setCoordsByGPS(gpsCoords);
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
		
		public double getZ() {
			return z;
		}

		public void setZ(double z) {
			this.z = z;
		}
		

		/**
		 * Geodetic to Cartesian Conversion formula:
		 * x = R * cos(lat) * cos(lon)
		 * y = R * cos(lat) * sin(lon)
		 * z = R *sin(lat)
		 * 
		 * R = Earth radius in KM (6371)
		 * 
		 * @param gpsCoords
		 */
		
		public void setCoordsByGPS(GpsCoords gpsCoords) {
			double dLat = Math.toRadians(gpsCoords.getLatitude());
		    double dLng = Math.toRadians(gpsCoords.getLongitude());
			this.x = R * Math.cos(dLat) * Math.cos(dLng);
			this.y = R * Math.cos(dLat) * Math.sin(dLng);
			this.z = R * Math.sin(dLat);
		}
		
		public void setCoordsByGPS(double latitude, double longitude) {
			double dLat = Math.toRadians(latitude);
		    double dLng = Math.toRadians(longitude);
			this.x = R * Math.cos(dLat) * Math.cos(dLng);
			this.y = R * Math.cos(dLat) * Math.sin(dLng);
			this.z = R * Math.sin(dLat);
		}
		
		/**
		 * Cartesian to Geodetic Conversion formula:
		 * lat = asin(z / R)
		 * lon = atan2(y, x)
		 * 
		 * @return
		 */
		public GpsCoords getGPSCoords() {
			return coords;
		}
		
		public double distance(CartesianCoord other) {
			double y = ( this.getY() - other.getY() ) * ( this.getY() - other.getY() );
			double x = ( (this.getX() - other.getX()) * (this.getX() - other.getX()));
			return Math.sqrt(y + x);
		}
		
		@Override
		public String toString() {
			return String.format("[%f,%f,%f]", x,y,z);
		}
	}
	
	
	@Override
	public String toString() {
		return String.format("[%f,%f,%f]", this.latitude, this.longitude, this.altitude);
	}
}
