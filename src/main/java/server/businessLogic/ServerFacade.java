package server.businessLogic;

import java.util.ArrayList;

import server.entities.Area;
import server.entities.Intersection;
import server.entities.User;

public class ServerFacade {
	
	private static ServerFacade serverFacade = null;
	private ArrayList<Area> areas = null;
	
	private ServerFacade() {
		areas = new ArrayList<>();
	}
	
	public static ServerFacade getInstance(){
		if(serverFacade == null)
			serverFacade = new ServerFacade();
		return serverFacade;
	}
	
	public ArrayList<Intersection> getIntersections(){
		ArrayList<Intersection> allIntersections = new ArrayList<>();
		
		for (Area area : areas) {
			allIntersections.addAll(area.getIntersections());
		}
		return allIntersections;
	}

	public ArrayList<Area> getAreas() {
		return areas;
	}

	public boolean setAreas(ArrayList<Area> areas) {
		if(areas != null && !areas.isEmpty()) {
			this.areas.addAll(areas);
			return true;
		}
		return false;
	}
	
	public String setUser(User user) throws Exception {
		if(user.getToken() !=null && !user.getToken().isEmpty()) {
			
			
			return null;
		}else {
			throw new Exception("token is empty or null.");
		}
	}
	
	
}
