package server.businessLogic;

import org.springframework.web.bind.annotation.RequestBody;

import server.entities.User;

public interface IRestController {

	String sendUserData(@RequestBody User user);
	String getIntersections();
	void userExit(User user);
}
