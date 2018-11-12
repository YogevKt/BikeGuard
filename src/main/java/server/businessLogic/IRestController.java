package server.businessLogic;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import server.entities.GpsCoords;
import server.entities.Intersection;

public interface IRestController {

	ResponseEntity<String> updateGpsCoords(@PathVariable("token") String token, @RequestBody GpsCoords userCoords);
	ArrayList<Intersection> getIntersection();
}
