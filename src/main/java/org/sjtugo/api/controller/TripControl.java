package org.sjtugo.api.controller;

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

import java.util.Optional;


@Api(value="Start Trip")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/trip")
public class TripControl {
    @Autowired
    private TripRepository tripRepository;

    @ApiOperation(value = "开始行程", notes = "选定一个strategy，返回行程ID")
    @PostMapping("/start")
    public Integer startTrip(@RequestBody StrategyRequest strategyRequest){
        TripService tripService = new TripService(tripRepository);
        return tripService.startTrip(strategyRequest.getStrategy(),
                strategyRequest.getUserID());
    }

    @ApiOperation(value = "查询行程", notes = "输入行程ID，返回详细信息")
    @GetMapping("/get/id={tripID}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Trip.class),
            @ApiResponse(code = 404, message = "[4]Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> startTrip(@PathVariable("tripID") Integer tripID){
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
        private Integer userID; //前端
    }
}
