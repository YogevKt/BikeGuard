package server.businessLogic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import server.entities.Intersection;
import server.entities.User;

@org.springframework.web.bind.annotation.RestController
public class RestController implements IRestController{
	
	@Override
	@RequestMapping(value="userExit", method = RequestMethod.POST)
	public void userExit(@RequestBody User user) {
		if(user != null && !user.getToken().isEmpty()) {
			UserAlertsService.getInstance().removeUserAlert(user.getToken());
			ServerFacade.getInstance().removeUserFromIntersection(user);
		}
	}
	
	@Override
	@RequestMapping(value="getIntersection", method = RequestMethod.GET)
	public List<Intersection> getIntersection() {
		
		return ServerFacade.getInstance().getIntersections();
	}
	
	@Override
	@RequestMapping(value="getIntersectionsCoords", method = RequestMethod.GET)
	public String getIntersections() { 
		System.err.println("getAreas");

		return new Gson().toJson(ServerFacade.getInstance().getIntersections());
	}
	
	@Override
	@RequestMapping(value = "addIntersection", method = RequestMethod.POST)
	public void addIntersection(@RequestBody Intersection intersection){
		try {
			ServerFacade.getInstance().addIntersection(intersection);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	@Override
	@RequestMapping(value = "sendUserData", method = RequestMethod.POST)
	public String sendUserData(@RequestBody User user){
		String respone;

		try {
			if (user != null && user.getCoords() != null && !user.getToken().isEmpty()) {
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
