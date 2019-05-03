package server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import server.businessLogic.ServerServices;

@SpringBootApplication
public class BikeGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeGuardApplication.class, args);

		new Thread(new ServerServices()).start();
		
	}
}
