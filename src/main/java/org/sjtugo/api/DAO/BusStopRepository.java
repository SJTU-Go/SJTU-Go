package org.sjtugo.api.DAO;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BusStopRepository extends JpaRepository<BusStop, Integer> {

    @Query(value = "from BusStop u where u.stopId > 1 ORDER BY u.stopId ASC")
    List<BusStop> getAllCounterStartBus();

    @Query(value = "from BusStop u where u.stopId > 0 and u.stopId < 19 ORDER BY u.stopId ASC")
    List<BusStop> getAllCounterArriveBus();

    List<BusStop> findByStopIdBetween(Integer a, Integer b);

}

