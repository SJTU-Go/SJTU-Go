package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip,Integer> {
}
