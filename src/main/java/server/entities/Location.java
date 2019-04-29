package server.entities;


public abstract class Location implements ILocation{
	
	public static final int INTERSECTION_NOTIFICATION_DISTANCE = 100;
	public static final int AREA_DISTANCE_RADIUS = 1000;
	public static final double HIGH_ALERT_DISTANCE = 35;
	public static final double MEDIUM_ALERT_DISTANCE = 75;
	
	private double longitude;
	private double latitude;
	
	
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
			double res = distance(coordA.getLatitude(), coordA.getLongitude(), coordB.getLatitude(), coordB.getLongitude());
			return res/1000.0;
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
	
	public static class CartesianCoord{
		private double x;
		private double y;
		private double z;
		
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
			this.x = R * Math.cos(gpsCoords.getLatitude()) * Math.cos(gpsCoords.getLongitude());
			this.y = R * Math.cos(gpsCoords.getLatitude()) * Math.sin(gpsCoords.getLongitude());
			this.z = R * Math.sin(gpsCoords.getLatitude());
		}
		
		public void setCoordsByGPS(double latitude, double longitude) {
			this.x = R * Math.cos(latitude) * Math.cos(longitude);
			this.y = R * Math.cos(latitude) * Math.sin(longitude);
			this.z = R * Math.sin(latitude);
		}
		
		/**
		 * Cartesian to Geodetic Conversion formula:
		 * lat = asin(z / R)
		 * lon = atan2(y, x)
		 * 
		 * @return
		 */
		public GpsCoords getGPSCoords() {
			return new GpsCoords(Math.asin(this.z/R), Math.atan2(this.y, this.x));
		}
		
		public double distance(CartesianCoord other) {
			double y = ( this.getY() - other.getY() ) * ( this.getY() - other.getY() );
			double x = ( (this.getX() - other.getX()) * (this.getX() - other.getX()));
			return Math.sqrt(y + x);
		}
	}
	
	
	
	
	@Override
	public String toString() {
		return String.format("[%f,%f]", this.latitude, this.longitude);
	}
}
