package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.entity.Route;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

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

    @Data
    private static class StrategyRequest {
        private Strategy strategy;
        private Integer userID; //前端
    }
}
