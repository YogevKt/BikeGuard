package server.businessLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import server.entities.User;

@org.springframework.web.bind.annotation.RestController
public class RestController implements IRestController{
	@Autowired
	private ServerFacade serverFasade;
	
	@Override
	@RequestMapping(value="userExit", method = RequestMethod.POST)
	public void userExit(@RequestBody User user) {
		if(user != null && !user.getToken().isEmpty()) {
			UserAlertsService.getInstance().removeUserAlert(user.getToken());
			serverFasade.removeUserFromIntersection(user);
		}
	}
	
	@Override
	@RequestMapping(value="getIntersectionsCoords", method = RequestMethod.GET)
	public String getIntersections() { 
		return new Gson().toJson(serverFasade.getIntersections());
	}
	
	@Override
	@RequestMapping(value = "sendUserData", method = RequestMethod.POST)
	public String sendUserData(@RequestBody User user){
		String respone;

		try {
			if (user != null && user.getCoords() != null && !user.getToken().isEmpty()) {
				respone = serverFasade.setUser(user);
			}else {
				return "User data did not send correctly";
			}
		} catch (Exception e) {
			return e.getMessage();
		}

		return "User "+respone;
	}
	
	
	
}
