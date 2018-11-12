package server.businessLogic;

import java.util.ArrayList;

import server.entities.Area;
import server.entities.Intersection;

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
		
		for (Area area : areas) 
			allIntersections.addAll(area.getIntersections());
		
		
		return allIntersections;
	}


	public ArrayList<Area> getAreas() {
		return areas;
	}


	public void setAreas(ArrayList<Area> areas) {
		this.areas = areas;
	}
	
	
	
	
}
