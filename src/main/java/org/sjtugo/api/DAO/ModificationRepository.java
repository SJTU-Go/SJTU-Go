package org.sjtugo.api.DAO;

import io.swagger.models.auth.In;
import org.sjtugo.api.entity.Modification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModificationRepository extends JpaRepository<Modification, Integer> {
    List<Modification> findByAdminID(Integer adminID);
}
