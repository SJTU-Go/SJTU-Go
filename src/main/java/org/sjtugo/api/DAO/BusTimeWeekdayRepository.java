package org.sjtugo.api.DAO;

import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;

@Table(name = "bus_time_weekday")
public interface BusTimeWeekdayRepository extends CrudRepository<BusTimeWeekday,Integer> {
}
