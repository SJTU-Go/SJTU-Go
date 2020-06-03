package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip,Integer> {
    List<Trip> findByUserID(Integer userID);
}
