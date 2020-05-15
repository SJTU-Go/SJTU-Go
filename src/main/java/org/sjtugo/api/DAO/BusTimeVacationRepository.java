package org.sjtugo.api.DAO;

import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;

@Table(name = "bus_time_vacation")
public interface BusTimeVacationRepository extends CrudRepository<BusTimeVacation,Integer> {
}
