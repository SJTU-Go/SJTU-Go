package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.DAO.ScheduleInfoRepository;
import org.sjtugo.api.entity.*;
import org.sjtugo.api.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private VipRepository vipRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleInfoRepository scheduleInfoRepository;


    @ApiOperation(value = "用户登陆")
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        UserService userser = new UserService(userRepository);
        return userser.userLogin(loginRequest.getCode(),loginRequest.getName());
    }

    @ApiOperation(value = "上传用户日程")
    @PostMapping("/updateScheduleInfo")
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleInfoRequest scheduleInfoRequest){
        ScheduleInfoService scheduleInfoService = new ScheduleInfoService(scheduleInfoRepository);
        return scheduleInfoService.updateSchedule(scheduleInfoRequest.getSchedule(),
                scheduleInfoRequest.getUserID());
    }

    @ApiOperation(value = "获取用户日程信息")
    @PostMapping("/getScheduleInfo")
    public @ResponseBody Optional<ScheduleInfo> getScheduleInfo(@RequestParam Integer userID){
        ScheduleInfoService scheduleInfoService = new ScheduleInfoService(scheduleInfoRepository);
        return scheduleInfoService.getScheduleInfo(userID);
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

    @ApiOperation(value = "上传月卡信息")
    @PostMapping("/vip/add")
    public ResponseEntity<?> addVip(@RequestBody VipRequest vipRequest){
        VipService vipService = new VipService(vipRepository);
        return vipService.addVip(vipRequest.getUserID(),
                vipRequest.getViplist());
    }

    @ApiOperation(value = "获取用户月卡设定")
    @PostMapping("/vip/get")
    public @ResponseBody
    Optional<Vip> getVip(@RequestParam Integer userID){
        VipService vipService = new VipService(vipRepository);
        return vipService.getVip(userID);
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
                scheduleRequest.getDepartShow(),
                scheduleRequest.getArriveShow(),
                scheduleRequest.getDepart(),
                scheduleRequest.getArrive());
    }

    @ApiOperation(value = "获取日程信息")
    @PostMapping("/schedule/get")
    public @ResponseBody List<Schedule> getScheduleList(@RequestParam Integer userID){
        ScheduleService scheduleService = new ScheduleService(scheduleRepository);
        return scheduleService.getScheduleList(userID);
    }


    @Data
    static class LoginRequest{
        @ApiModelProperty(value = "登陆凭证", example = "123f334d")
        private String code;
        @ApiModelProperty(value = "用户名", example = "nicolas")
        private String name;
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
    static class VipRequest{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;
        @ApiModelProperty(value = "月卡列表", example="['哈罗单车']")
        private String viplist;

    }

    @Data
    static class ScheduleInfoRequest{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;
        @ApiModelProperty(value = "用户日程信息", example="{\"schedule\":[{\"time\":\"12\"},{\"time\":\"13\"}]}")
        private JSONObject schedule;
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
        @ApiModelProperty(value = "起点", example="信息楼")
        private String departShow;
        @ApiModelProperty(value = "终点", example="激光楼E楼")
        private String arriveShow;
        @ApiModelProperty(value = "起点编码", example="DT137348")
        private String depart;
        @ApiModelProperty(value = "终点编码", example="DT137224")
        private String arrive;
    }







}
    