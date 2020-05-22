package org.sjtugo.api.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;
import java.util.List;

@Table(name = "bus_time")
public interface BusTimeRepository extends JpaRepository<BusTime,Integer> {
    List<BusTime> findAllByBusTypeEquals(Integer a);
}
