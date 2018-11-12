package server.businessLogic;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.entities.GpsCoords;
import server.entities.Intersection;

@org.springframework.web.bind.annotation.RestController
public class RestController implements IRestController{
	
	@Override
	@RequestMapping(value="updateGPSCoords/{token}", method = RequestMethod.POST)
	public ResponseEntity<String> updateGpsCoords(@PathVariable("token") String token, @RequestBody GpsCoords userCoords) {
		System.err.println(token+" "+userCoords);
		return new ResponseEntity<String>("Coords updated", HttpStatus.CREATED);
	}
	
	@Override
	@RequestMapping(value="getIntersections", method = RequestMethod.GET)
	public ArrayList<Intersection> getIntersection() { 
		return ServerFacade.getInstance().getIntersections();
	}
	

	
	@RequestMapping(value="check", method = RequestMethod.GET)
	public String check() { 
		return "Hi May :D";
	}
	
}
