package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.BusTime;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Table;
import java.util.List;

@Table(name = "bus_time")
public interface BusTimeRepository extends JpaRepository<BusTime,Integer> {
    List<BusTime> findAllByBusTypeEquals(Integer a);
}
