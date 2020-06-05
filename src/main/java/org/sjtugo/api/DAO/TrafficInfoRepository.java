package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Admin;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface TrafficInfoRepository extends JpaRepository<TrafficInfo, Integer> {

    public List<TrafficInfo> findAllByBeginTimeIsBeforeAndEndTimeIsAfter(LocalTime t1, LocalTime t2);
}
