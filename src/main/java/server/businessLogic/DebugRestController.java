package server.businessLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import server.entities.Area;
import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.User;

@RestController
@RequestMapping("/debug")
public class DebugRestController {
	@Autowired
	private ServerFacade serverFasade;

	@RequestMapping(value = "initDB", method = RequestMethod.GET)
	public void debug() {

		try {

			// add area

			Area area1 = new Area(32.113068, 34.81804, 22); // Afeka
			serverFasade.addArea(area1);
			Area area2 = new Area(32.124555, 34.855949, 21); // Morasha
			serverFasade.addArea(area2);

			// add intersection nearby Afeka
			serverFasade.addIntersection(new Intersection(32.113622, 34.816944, 22));// Afeka intersection
			serverFasade.addIntersection(new Intersection(32.119251, 34.810396, 22));// KKL intersection

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "addIntersection", method = RequestMethod.POST)
	public void addIntersection(@RequestBody Intersection intersection) {
		try {
			serverFasade.addIntersection(intersection);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@RequestMapping(value = "testCollision", method = RequestMethod.GET)
	public void testConversion() {

		User user1 = new User(
				"dWv1T0x_mB4:APA91bGTP78G8e7MnQTT8aIdfy2nLxAoXR2YS8_VO3Dgr_fx2Jpkl4PxaQ_3WS5nKtwPix8gFAXlwUkdS61SF8Z9Ugn-FaMhvCUMA2KAPZYQEQJv91I0_O3NdlTufo3guQ7Cgo2IBREa",
				User.UserType.DRIVER, new GpsCoords(32.113604, 34.817115, 22));
		user1.setSpeed(20);
		user1.setCoords(new GpsCoords(32.11334, 34.817512, 22));

		User user2 = new User(
				"fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
				User.UserType.BIKER, new GpsCoords(32.113504, 34.818458, 22));
		user2.setCoords(new GpsCoords(32.113309, 34.818043, 22));
		user2.setSpeed(20);

		try {
			serverFasade.setUser(user1);
			serverFasade.setUser(user2);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "temp", method = RequestMethod.GET)
	public void temp() {
		try {
			Area area1 = new Area(32.267527, 34.911479, 43); // Yogev
			serverFasade.addArea(area1);
			Area area2 = new Area(31.968026, 34.801616, 50); // May
			serverFasade.addArea(area2);
			
			serverFasade.addIntersection(new Intersection(31.968102, 34.80165, 50));//May's intersection
			serverFasade.addIntersection(new Intersection(32.267595, 34.911456, 43));//Yogev's intersection
			

		} catch (Exception e) {
		}
	}
	
	
	@RequestMapping(value = "fakeDrivers", method = RequestMethod.GET)
	public void voidFakeUsers() {
		//may
		User maysArea = new User(
				"dSv1T0x_mB4:APA91bGTP78G8e7MnQTT8aIdfy2nLxAoXR2YS8_VO3Dgr_fx2Jpkl4PxaQ_3WS5nKtwPix8gFAXlwUkdS61SF8Z9Ugn-FaMhvCUMA2KAPZYQEQJv91I0_O3NdlTufo3guQ7Cgo2IBREa",
				User.UserType.DRIVER, new GpsCoords(31.968086, 34.801468, 50));
		maysArea.setSpeed(30);
		maysArea.setCoords(new GpsCoords(31.968088, 34.801533, 50));
		
		
		
		//yogev
		User yogevsArea = new User(
				"dSA1T0x_mB4:APA91bGTP78G8e7MnQTT8aIdfy2nLxAoXR2YS8_VO3Dgr_fx2Jpkl4PxaQ_3WS5nKtwPix8gFAXlwUkdS61SF8Z9Ugn-FaMhvCUMA2KAPZYQEQJv91I0_O3NdlTufo3guQ7Cgo2IBREa",
				User.UserType.DRIVER, new GpsCoords(32.267537, 34.911343, 43));
		yogevsArea.setSpeed(30);
		yogevsArea.setCoords(new GpsCoords(32.267559, 34.911412, 43));
		
		try {
			serverFasade.setUser(maysArea);
			serverFasade.setUser(yogevsArea);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
