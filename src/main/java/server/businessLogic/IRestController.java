package server.businessLogic;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;

import server.entities.Intersection;
import server.entities.User;

public interface IRestController {

	ArrayList<Intersection> getIntersection();
	String sendUserData(@RequestBody User user);
	void addIntersection(@RequestBody Intersection intersection);
	public String getIntersections();
	public void userExit(User user);
}
