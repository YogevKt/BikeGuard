package server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import server.businessLogic.ServerFacade;
import server.businessLogic.ServerServices;
import server.entities.Area;
import server.entities.GpsCoords;
import server.entities.Intersection;
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
			server.addArea(new Area(31.96828999958059,34.80140645543159));//area may
			server.addArea(new Area(32.268066,34.911650));//area yogev
			
			//add intersections	nearby may's home		
			server.addIntersection(new Intersection(31.968111,34.800483)); //76~ from may's home
			server.addIntersection(new Intersection(31.968210,34.800753)); //46~ from may's home
			server.addIntersection(new Intersection(31.96828999958059,34.80140645543159)); //17~ from may's home
			
			//add intersection nearby yogev's home
			server.addIntersection(new Intersection(32.268066, 34.911650));//95~ from yogev's home
			server.addIntersection(new Intersection(31.267475, 34.911670));//44~ from yogev's home
		
			
			//add driver
			/*server.setUser(new User("clX5g_VUGCU:APA91bGhM-A_a8C_nGHCi1-KkleO40Zt9k3X9v1fx58zmLI8oS3e1_1bQrToPqqq1dRniPHIekjzCS9MYUHIp_-k8pRWzUzwwnsMhpOqrlk45mtkcjyew0XaTm0wtdjcFWSBZmJbOITr",
					User.UserType.DRIVER, new GpsCoords(31.968210,34.800753)));*/
			
			
			server.setUser(new User("fAfBRGDJpf8:APA91bELeW-CBV1fJm8SqS8H-cDW7Gn7VX1yeAW0xyWpkDfECIcQSzCa0-2rP52D3NTDqIGhTT_TgYSapeGaEX7ks5WIfGlZO-dDvuRzfqAEjRNmfBNv3ToIj_l90eTkt0E08-ekQmL1",
					User.UserType.BIKER, new GpsCoords(32.267659,34.911553)));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
