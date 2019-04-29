package server.businessLogic;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import server.entities.Intersection;
import server.entities.User;

public interface IRestController {

	List<Intersection> getIntersection();
	String sendUserData(@RequestBody User user);
	void addIntersection(@RequestBody Intersection intersection);
	public String getIntersections();
	public void userExit(User user);
}
