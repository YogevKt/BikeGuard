package server.dal;

import org.springframework.beans.factory.annotation.Autowired;
import server.entities.Area;

import java.util.ArrayList;
import java.util.List;

public class AreasDao {

    @Autowired
    private AreaRepo areaRepo;

    public void save(Area area){
        areaRepo.save(area);
    }

    public List<Area> getAreas(){
        List<Area> list = new ArrayList<>();
        areaRepo.findAll().forEach(list::add);
        return list;
    }
}
