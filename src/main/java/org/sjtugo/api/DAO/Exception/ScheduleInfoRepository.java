package org.sjtugo.api.DAO;


import org.sjtugo.api.entity.ScheduleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfo,Integer> {
}

