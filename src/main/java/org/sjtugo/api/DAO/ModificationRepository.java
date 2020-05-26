package org.sjtugo.api.DAO;

import io.swagger.models.auth.In;
import org.sjtugo.api.entity.Modification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModificationRepository extends JpaRepository<Modification, Integer> {

}
