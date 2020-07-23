package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Punishment;
import org.sjtugo.api.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PunishmentRepository extends JpaRepository<Punishment,Integer> {
}
