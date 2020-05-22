package org.sjtugo.api.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination,Integer> {
    List<Destination> findByPlaceNameLike(String kw);
}
