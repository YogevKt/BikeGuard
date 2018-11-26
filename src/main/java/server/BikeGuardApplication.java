package server;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import server.businessLogic.ServerFacade;
import server.businessLogic.ServerServices;
import server.entities.Area;
import server.entities.Intersection;

@SpringBootApplication
public class BikeGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeGuardApplication.class, args);
		
		//debug
		debug();

		new Thread(new ServerServices()).start();
		
	}
	
	public static void debug() {
		ServerFacade server = ServerFacade.getInstance();
		
		try {
			
			//add area
			server.addArea(new Area(1,1));
			
			//add intersections
			
			server.addIntersection(new Intersection(2, 2));
			server.addIntersection(new Intersection(3, 3));
			server.addIntersection(new Intersection(4, 4));
			server.addIntersection(new Intersection(5, 5));
			
			
			
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
