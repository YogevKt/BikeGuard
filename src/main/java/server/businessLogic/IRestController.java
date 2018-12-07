package server.businessLogic;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import server.entities.GpsCoords;
import server.entities.Intersection;
import server.entities.User;

public interface IRestController {

	ArrayList<Intersection> getIntersection();
	String sendUserData(@RequestBody User user);
	void addIntersection(@RequestBody Intersection intersection);
}
