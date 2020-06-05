package org.sjtugo.api.service;

import org.sjtugo.api.DAO.ScheduleRepository;
import org.sjtugo.api.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }

    public ResponseEntity<?> addSchedule(Integer userID, String yearMonth, String selectDay,
                                         String timehour, String timeminute, String schedulename, String place){
        Schedule newSchedule = new Schedule();
        newSchedule.setUserID(userID);
        newSchedule.setYearMonth(yearMonth);
        newSchedule.setSelectDay(selectDay);
        newSchedule.setTimehour(timehour);
        newSchedule.setTimeminute(timeminute);
        newSchedule.setSchedulename(schedulename);
        newSchedule.setPlace(place);
        scheduleRepository.save(newSchedule);

        return new ResponseEntity<>(newSchedule, HttpStatus.OK);
    }

    public List<Schedule> getScheduleList(Integer userID){
        return scheduleRepository.findByUserID(userID);
    }
}
