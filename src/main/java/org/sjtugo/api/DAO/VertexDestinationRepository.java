package org.sjtugo.api.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VertexDestinationRepository extends JpaRepository<VertexDestination,VertexDestinationID> {

    VertexDestination findByVertexid(Integer id);

    VertexDestination findByPlaceid(Integer id);

}
