package org.sjtugo.api.service;

import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.ScheduleInfoRepository;
import org.sjtugo.api.entity.ScheduleInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class ScheduleInfoService {
    private final ScheduleInfoRepository scheduleInfoRepository;

    public ScheduleInfoService(ScheduleInfoRepository scheduleInfoRepository){
        this.scheduleInfoRepository = scheduleInfoRepository;
    }

    public ResponseEntity<?> updateSchedule(JSONObject schedule, Integer userID){
        ScheduleInfo newSchedule = new ScheduleInfo();

        newSchedule.setUserID(userID);
        newSchedule.setSchedule(schedule.toString());
        scheduleInfoRepository.save(newSchedule);

        return new ResponseEntity<>(newSchedule, HttpStatus.OK);
    }

    public Optional<ScheduleInfo> getScheduleInfo(Integer userID){
        return scheduleInfoRepository.findById(userID);
    }
}
