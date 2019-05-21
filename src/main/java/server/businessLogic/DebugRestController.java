package server.businessLogic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
			/*
			 * Area area1 = new Area(31.96828999958059,34.80140645543159);
			 * serverFasade.addArea(area1);//area may Area area2 = new
			 * Area(32.268066,34.911650); serverFasade.addArea(area2);//area yogev
			 */

			// add intersections nearby may's home
			/*
			 * serverFasade.addIntersection(new Intersection(31.968111,34.800483)); //76~
			 * from may's home serverFasade.addIntersection(new
			 * Intersection(31.968210,34.800753)); //46~ from may's home
			 * serverFasade.addIntersection(new
			 * Intersection(31.96828999958059,34.80140645543159)); //17~ from may's home
			 */

			// add intersection nearby yogev's home
			/*
			 * serverFasade.addIntersection(new Intersection(32.255333, 34.891211));//Beni
			 * dror intersection( from yogev's home) serverFasade.addIntersection(new
			 * Intersection(32.260859, 34.913738));//Tel mond intersection (from yogev's
			 * home)
			 */

			// add driver
			User user1 = new User(
					"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
					User.UserType.DRIVER, new GpsCoords(32.255524, 34.892249));
			user1.setSpeed(20);
			serverFasade.setUser(user1);
			User user2 = new User(
					"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
					User.UserType.BIKER, new GpsCoords(32.25513, 34.890398));
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
				User.UserType.DRIVER, new GpsCoords(32.255402, 34.891479));
		user1.setSpeed(30);
		serverFasade.setUser(user1);
		User user2 = new User(
				"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
				User.UserType.BIKER, new GpsCoords(32.255229, 34.890691));
		user2.setSpeed(30);
		serverFasade.setUser(user2);
	}

	@RequestMapping(value = "testDistance", method = RequestMethod.GET)
	public String testDistance() throws Exception {
		StringBuilder sb = new StringBuilder();
		User user1 = new User(
				"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
				User.UserType.DRIVER, new GpsCoords(32.255524, 34.892249));
		User user2 = new User(
				"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
				User.UserType.BIKER, new GpsCoords(32.25513, 34.890398));

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
		User user1 = new User(
				"clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
				User.UserType.DRIVER, new GpsCoords(32.255229, 34.890691));
		CartesianCoord userCart = new CartesianCoord(user1.getCoords());

		sb.append("User coords: " + user1.getCoords() + "\n");
		sb.append("Cart: " + userCart + "\n");
		sb.append("cart to coords: " + userCart.getGPSCoords() + "\n");
		sb.append("Test" + testFun(userCart) + "\n");
		

		return sb.toString();
	}
	
	public static final double a = 6378.137;
	public static final double b = 6.3568e6;
	public static final double e = Math.sqrt((Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2));
	public static final double e2 = Math.sqrt((Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(b, 2));

	public GpsCoords testFun(CartesianCoord coords) {

	    double[] lla = { 0, 0, 0 };
	    double lat, lon, height, N , p;
	    double x=coords.getX(),y=coords.getY(),z=coords.getZ();

	    p = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

	    lon = Math.atan(y / x);

	    lat = Math.asin(z/6378.137);
	    N = a / (Math.sqrt(1 - (Math.pow(e, 2) * Math.pow(Math.sin(lat), 2))));

	    double m = (p / Math.cos(lat));
	    height = m - N;


	    lon = lon * 180 / Math.PI;
	    lat = lat * 180 / Math.PI; 
	    lla[0] = lat;
	    lla[1] = lon;
	    lla[2] = height;
	    return new GpsCoords(lat, lon);
	}
}
