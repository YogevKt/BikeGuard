package server.businessLogic;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import server.entities.GpsCoords;

@Component
public class RestService {

    private RestTemplate restTemplate;
    private final String URL = "http://127.0.0.1:5000";

    public RestService() {
        restTemplate = new RestTemplate();
    }

    public CartesianCoord convertGpsCoordsToCartesianCoord(GpsCoords gpsCoords){
        String URI ="/geodetic_to_cartesian";
        HttpEntity<GpsCoords> request = new HttpEntity<>(gpsCoords);
        return restTemplate.postForEntity(this.URL+URI, request, CartesianCoord.class).getBody();
    }

    public GpsCoords convertCartesianToGpsCoords(CartesianCoord cartesianCoord){
        String URI ="/cartesian_to_geodetic";
        HttpEntity<CartesianCoord> request = new HttpEntity<>(cartesianCoord);
        return restTemplate.postForEntity(this.URL+URI, request, GpsCoords.class).getBody();
    }

}
