package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Admin;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficInfoRepository extends JpaRepository<TrafficInfo, Integer> {
}
