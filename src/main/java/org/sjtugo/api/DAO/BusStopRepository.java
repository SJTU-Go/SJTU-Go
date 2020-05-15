package org.sjtugo.api.DAO;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BusStopRepository extends CrudRepository<BusStop, Integer> {

    @Query(value = "from BusStop u where u.stopId > 0 ORDER BY u.stopId ASC")
    List<BusStop> getAllCounterBus();
}

