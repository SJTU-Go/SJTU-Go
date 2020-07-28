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
                                         String timehour, String timeminute, String schedulename, String depart, String arrive, String departShow, String arriveShow){
        Schedule newSchedule = new Schedule();
        newSchedule.setUserID(userID);
        newSchedule.setYearMonh(yearMonth);
        newSchedule.setSelectDay(selectDay);
        newSchedule.setTimeHour(timehour);
        newSchedule.setTimeMinute(timeminute);
        newSchedule.setScheduleName(schedulename);
        newSchedule.setDepart(depart);
        newSchedule.setArrive(arrive);
        newSchedule.setDepartShow(departShow);
        newSchedule.setArriveShow(arriveShow);
        scheduleRepository.save(newSchedule);

        return new ResponseEntity<>(newSchedule, HttpStatus.OK);
    }

    public List<Schedule> getScheduleList(Integer userID){
        return scheduleRepository.findByUserID(userID);
    }
}
