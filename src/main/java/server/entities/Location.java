package server.entities;


public abstract class Location implements ILocation{
	
	public static final int INTERSECTION_NOTIFICATION_DISTANCE = 100;
	public static final int AREA_DISTANCE_RADIUS = 1000;
	
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
			double x = (coordB.getLongitude()-coordA.getLongitude())*(coordB.getLongitude()-coordA.getLongitude());
			double y = (coordB.getLatitude()-coordA.getLatitude())*(coordB.getLatitude()-coordA.getLatitude());
			
			return Math.abs(Math.sqrt(x+y));
			/*double res = distance(coordA.getLatitude(), coordA.getLongitude(), coordB.getLatitude(), coordB.getLongitude());
			System.err.println(">>>>>>>>>>dis = "+res);
			return res;*/
		}else {
			throw new Exception("Coord A or B was null");
		}
	}
	
	
	@Override
	public String toString() {
		return String.format("[%f,%f]", this.longitude, this.latitude);
	}
	
	public static double distance(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6.371; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	}
}
