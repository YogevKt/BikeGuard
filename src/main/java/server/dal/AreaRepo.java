package server.dal;

import org.springframework.data.repository.CrudRepository;

import server.entities.Area;

public interface AreaRepo extends CrudRepository<Area, Long>{
	
}
