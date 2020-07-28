package org.sjtugo.api.controller;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.*;
import lombok.Data;
import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Feedback;
import org.sjtugo.api.entity.Route;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.Trip;
import org.sjtugo.api.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Optional;


@Api(value="Start Trip")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/trip")
public class TripControl {
    @Autowired
    private TripRepository tripRepository;

    @ApiOperation(value = "开始行程", notes = "选定一个strategy，返回行程ID")
    @PostMapping("/start")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Integer.class),
            @ApiResponse(code = 404, message = "[4]Invalid Trip", response = ErrorResponse.class)
    })
    public ResponseEntity<?> startTrip(@RequestBody StrategyRequest strategyRequest){
        TripService tripService = new TripService(tripRepository);
        return tripService.startTrip(strategyRequest.getStrategy(),
                    strategyRequest.getUserID(),strategyRequest.getDepartTime());
    }

    @ApiOperation(value = "查询行程", notes = "输入行程ID，返回详细信息")
    @GetMapping("/get/id={tripID}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Trip.class),
            @ApiResponse(code = 404, message = "[4]Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> findTrip(@PathVariable("tripID") Integer tripID){
        TripService tripService = new TripService(tripRepository);
        Optional<Trip> result = tripService.findTrip(tripID);
        if (result.isPresent()){
            return new ResponseEntity<Trip>(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<ErrorResponse>(
                    new ErrorResponse(4,"Not Found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Data
    static class StrategyRequest {
        private JSONObject strategy;
        private Integer userID;
        @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime departTime = LocalDateTime.now();//前端
    }
}
