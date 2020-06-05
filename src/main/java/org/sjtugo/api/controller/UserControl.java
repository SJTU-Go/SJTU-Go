package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.PreferenceRepository;
import org.sjtugo.api.DAO.ScheduleRepository;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.entity.Preference;
import org.sjtugo.api.entity.Schedule;
import org.sjtugo.api.entity.Trip;
import org.sjtugo.api.service.HistoryService;
import org.sjtugo.api.service.PreferenceService;
import org.sjtugo.api.service.ScheduleService;
import org.sjtugo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Api(value="UserInfo System")
@RestController
@RequestMapping("/user")
public class UserControl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;


    @ApiOperation(value = "用户登陆")
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestParam String code){
        UserService userser = new UserService(userRepository);
        return userser.userLogin(code);
    }

    @ApiOperation(value = "上传偏好")
    @PostMapping("/preference/add")
    public ResponseEntity<?> addPreference(@RequestBody PreferenceRequest preferenceRequest){
        PreferenceService preferenceService = new PreferenceService(preferenceRepository);
        return preferenceService.addPreference(preferenceRequest.getUserID(),
                preferenceRequest.getPreferencelist(),
                preferenceRequest.getBanlist());
    }

    @ApiOperation(value = "获取用户偏好设定")
    @PostMapping("/preference/get")
    public @ResponseBody Optional<Preference> getPreference(@RequestParam Integer userID){
        PreferenceService preferenceService = new PreferenceService(preferenceRepository);
        return preferenceService.getPreference(userID);
    }


    @ApiOperation(value = "获取用户历史行程")
    @PostMapping("/history/get")
    public @ResponseBody List<Trip> getHistoryList(@RequestParam Integer userID){
        HistoryService historyService = new HistoryService(tripRepository);
        return historyService.getHistoryList(userID);
    }

    @ApiOperation(value = "上传日程")
    @PostMapping("/schedule/add")
    public ResponseEntity<?> addSchedule(@RequestBody ScheduleRequest scheduleRequest){
        ScheduleService scheduleService = new ScheduleService(scheduleRepository);
        return scheduleService.addSchedule(scheduleRequest.getUserID(),
                scheduleRequest.getYearMonth(),
                scheduleRequest.getSelectDay(),
                scheduleRequest.getTimehour(),
                scheduleRequest.getTimeminute(),
                scheduleRequest.getSchedulename(),
                scheduleRequest.getPlace());
    }

    @ApiOperation(value = "获取用户日程信息")
    @PostMapping("/schedule/get")
    public @ResponseBody List<Schedule> getScheduleList(@RequestParam Integer userID){
        ScheduleService scheduleService = new ScheduleService(scheduleRepository);
        return scheduleService.getScheduleList(userID);
    }


    @Data
    static class PreferenceRequest{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;
        @ApiModelProperty(value = "偏好排序", example="['步行','共享单车','校园巴士']")
        private String preferencelist;
        @ApiModelProperty(value = "禁止列表", example="[]")
        private String banlist;
    }

    @Data
    static class History{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;
        @ApiModelProperty(value = "出发地", example="上院")
        private String depart;
        @ApiModelProperty(value = "目的地", example="北区篮球场")
        private String arrive;
        @ApiModelProperty(value = "出发时间", example="2020-05-24 19:07:40")
        private LocalDateTime departTime;
        @ApiModelProperty(value = "到达时间", example="2020-05-24 19:14:43")
        private LocalDateTime arriveTime;
        @ApiModelProperty(value = "路径")
        private String route;
    }

    @Data
    static class ScheduleRequest{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;
        @ApiModelProperty(value = "年月", example="2020-5")
        private String yearMonth;
        @ApiModelProperty(value = "日", example="14")
        private String selectDay;
        @ApiModelProperty(value = "小时", example="12")
        private String timehour;
        @ApiModelProperty(value = "分钟", example="30")
        private String timeminute;
        @ApiModelProperty(value = "日程名称", example="年纪大会")
        private String schedulename;
        @ApiModelProperty(value = "地点", example="电院群楼三号楼")
        private String place;
    }







}
    