package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.VertexDestination;
import org.sjtugo.api.DAO.Entity.VertexDestinationID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VertexDestinationRepository extends JpaRepository<VertexDestination, VertexDestinationID> {

    VertexDestination findByVertexid(Integer id);

    VertexDestination findByPlaceid(Integer id);

}
