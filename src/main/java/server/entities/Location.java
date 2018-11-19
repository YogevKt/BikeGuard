package server.entities;


public abstract class Location implements ILocation{
	
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
		}else {
			throw new Exception("Coord A or B was null");
		}
	}
	
	
	@Override
	public String toString() {
		return String.format("[%lf,%lf]", this.longitude, this.latitude);
	}
}
