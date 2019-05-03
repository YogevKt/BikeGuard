package server.dal;

import org.springframework.data.repository.CrudRepository;

import server.entities.Area;
public interface AreaDao extends CrudRepository<Area, Long>{

	
}
