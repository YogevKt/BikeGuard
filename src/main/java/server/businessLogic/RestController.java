package server.businessLogic;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.User;
import server.entities.User.UserType;

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
	

	@RequestMapping(value="changeUserType", method = RequestMethod.POST)
	public ResponseEntity<String> changeUserType(@RequestBody Map<String, String> data) {
		
		if(data.containsKey("userType"))
			System.err.println(data.get("userType"));
		else
			System.err.println("userType does not exists");
		
		return new ResponseEntity<String>("User Type CREATED", HttpStatus.CREATED);
	}
	
	@RequestMapping(value="updateLocationCoords", method = RequestMethod.POST)
	public ResponseEntity<String> updateLocationCoords(@RequestBody Map<String, Double> data) {
		double latitude = 0, longitude = 0;
		if(data.containsKey("latitude"))
			latitude = data.get("latitude");
		else if(data.containsKey("longitude"))
			longitude = data.get("longitude");
		else
			System.err.println("latitude, longitude does not exists");
		System.err.println("latitude: "+latitude+" longitude: "+longitude);
		
		return new ResponseEntity<String>("Cords Updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public ResponseEntity<String> updateUser(@RequestBody User user){
		
		String respone;
		try {
			respone = ServerFacade.getInstance().setUser(user);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<String>("User "+respone,HttpStatus.OK);
	}
	
	////////////////////////////////
	
	@RequestMapping(value="checkMessage", method = RequestMethod.POST)
	public String checkMessage(@RequestBody User user) {
		
		FireBaseServiceHandler.sendPushNotification(new User(user.getToken(),
				user.getType(), user.getCoords()), "test notification", "this is a test.");
		
		return "Check Message Worked";
	}

	@RequestMapping(value = "sendUserData", method = RequestMethod.POST)
	public String sendUserData(@RequestBody User user){
		String respone;

		try {
			if (user != null) {
				respone = ServerFacade.getInstance().setUser(user);
			}else {
				return "User data did not send correctly";
			}
		} catch (Exception e) {
			return e.getMessage();
		}

		return "User "+respone;
	}
	
}
