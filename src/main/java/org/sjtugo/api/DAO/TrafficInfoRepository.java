package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficInfoRepository extends JpaRepository<Admin, Integer> {
}
