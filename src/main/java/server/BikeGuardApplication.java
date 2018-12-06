package server;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import server.businessLogic.ServerFacade;
import server.businessLogic.ServerServices;
import server.entities.Area;
import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;

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
			server.addIntersection(new Intersection(34.800483,31.968111)); //76~ from may's home
			server.addIntersection(new Intersection(34.800753,31.968210)); //46~ from may's home
			server.addIntersection(new Intersection(34.801246,31.968244)); //17~ from may's home
			
//			//add driver
//			server.setUser(new User("clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
//					User.UserType.DRIVER, new GpsCoords(34.800753,31.968210)));
			
			GpsCoords a = new GpsCoords(34.80131358328928, 31.968252524058602);
			GpsCoords b = new GpsCoords(34.800753, 31.968210);
			
			System.err.println(">>>>>>>>>>>>>  Test distance - > "+ Location.distance(a,b));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
