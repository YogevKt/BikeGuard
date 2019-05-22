package server.businessLogic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import server.entities.Area;
import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.Location;
import server.entities.User;
import server.entities.Location.CartesianCoord;

@RestController
@RequestMapping("/debug")
public class DebugRestController {
	@Autowired
	private ServerFacade serverFasade;

	@RequestMapping(value = "debug", method = RequestMethod.GET)
	public void debug() {

		try {

			// add area
			
			 Area area1 = new Area(32.113068,34.81804,22); //Afeka
			 serverFasade.addArea(area1);
			 Area area2 = new Area(32.124555,34.855949,21); //Morasha
			 serverFasade.addArea(area2);
			
			 
			// add intersection nearby yogev's home
			  serverFasade.addIntersection(new Intersection(32.113622,34.816944,22));//Afeka intersection
			  serverFasade.addIntersection(new Intersection(32.119251,34.810396,22));//KKL intersection
			  
			  
			// add driver
			User user1 = new User(
					"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
					User.UserType.DRIVER, new GpsCoords(32.113604, 34.817115,22));
			user1.setSpeed(20);
			
			serverFasade.setUser(user1);
			User user2 = new User(
					"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
					User.UserType.BIKER, new GpsCoords(32.113504, 34.818458,22));
			user2.setSpeed(20);
			serverFasade.setUser(user2);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "debug2", method = RequestMethod.GET)
	public void debugUpdateUsers() throws Exception {
		
		User user1 = new User(
				"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
				User.UserType.DRIVER, new GpsCoords(32.11334, 34.817512,22));
		user1.setSpeed(30);
		serverFasade.setUser(user1);
		
		User user2 = new User(
				"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
				User.UserType.BIKER, new GpsCoords(32.113309, 34.818043,22));
		user2.setSpeed(30);
		serverFasade.setUser(user2);
	}

	@RequestMapping(value = "testDistance", method = RequestMethod.GET)
	public String testDistance() throws Exception {
		StringBuilder sb = new StringBuilder();
		User user1 = new User(
				"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
				User.UserType.DRIVER, new GpsCoords(32.113604, 34.817115,22));

		User user2 = new User(
				"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
				User.UserType.BIKER, new GpsCoords(32.113504, 34.818458,22));

		List<Intersection> intersections = serverFasade.getIntersections();
		sb.append("User coords: " + user1.getCoords() + "\n");
		System.out.println();
		for (Intersection intersection : intersections) {
			sb.append("Intersection coords: " + "[" + intersection.getLatitude() + "," + intersection.getLongitude()
					+ "]" + "\n");
			sb.append(Location.distance(intersection, user1.getCoords()) + "\n");
		}

		sb.append("\n\n");

		sb.append("User coords: " + user2.getCoords() + "\n");
		for (Intersection intersection : intersections) {
			sb.append("Intersection coords: " + "[" + intersection.getLatitude() + "," + intersection.getLongitude()
					+ "]" + "\n");
			sb.append(Location.distance(intersection, user2.getCoords()) + "\n\n");
		}

		return sb.toString();
	}

	@RequestMapping(value = "testConversion", method = RequestMethod.GET)
	public String testConversion() {
		
		StringBuilder sb = new StringBuilder();
//		User user1 = new User(
//				"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
//				User.UserType.DRIVER, new GpsCoords(32.113604, 34.817115,22));
//		CartesianCoord userCart = new CartesianCoord(user1.getCoords());
//
//		sb.append("User coords: " + user1.getCoords() + "\n");
//		sb.append("Cart: " + userCart + "\n");
//		sb.append("cart to coords: " + userCart.getGPSCoords() + "\n");
		
		Location.geodetic_to_cartesian(32.113604, 34.817115,22);
		return sb.toString();
	}

}
