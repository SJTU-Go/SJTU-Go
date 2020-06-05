package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.controller.ResponseEntity.TrafficInfoResponse;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.service.ModificationService;
import org.sjtugo.api.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api(value="Traffic Management System")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/traffic")
public class TrafficControl {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TrafficInfoRepository trafficInfoRepository;
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;

    @ApiOperation(value = "查找当前交通情况")
    @GetMapping("/current")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TrafficInfoResponse.class),
            @ApiResponse(code = 404, message = "[500]Error Message", response = ErrorResponse.class)
    })
    public ResponseEntity<?> currentTraffic() {
        TrafficService trafficService =
                new TrafficService(restTemplate, trafficInfoRepository, mapVertexInfoRepository);
//        try {
            return new ResponseEntity<>(trafficService.currentTraffic(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new ErrorResponse(e),HttpStatus.NOT_FOUND);
//        }
    }

}