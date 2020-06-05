package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
    List<Schedule> findByUserID(Integer userID);
}

