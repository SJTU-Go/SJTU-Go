package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination,Integer> {
    List<Destination> findByPlaceNameLike(String kw);
}
