package org.sjtugo.api.controller;


import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.*;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.entity.ErrorResponse;
import org.sjtugo.api.entity.HelloBikeInfo;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.service.MapInfoService;
import org.sjtugo.api.service.map.MapVertexResponse;
import org.sjtugo.api.service.planner.PlaceNotFoundException;
import org.sjtugo.api.service.planner.StrategyNotFoundException;
import org.sjtugo.api.service.planner.WalkPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api(value="地图信息查询服务系统")
@RestController
@RequestMapping("/map")
public class MapInfoControl {
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private HelloBikeRepository helloBikeRepository;


    @ApiOperation(value = "校园内地点查询",
            notes = "给定关键词，查询校园内建筑物信息")
    @GetMapping("/search/destination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Destination[].class)
    })
    public ResponseEntity<List<Destination>> searchPlace(@ApiParam(value = "关键词", example = "图书馆")
                                         @RequestParam String keyword) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                                        destinationRepository,helloBikeRepository);
        return new ResponseEntity<>(mapInfoService.searchPlace(keyword),HttpStatus.OK);
    }

    @ApiOperation(value = "校园内停车点查询（不含车辆数）",
            notes = "给定关键词，查询校园内停车点信息，一般用于搜索框")
    @GetMapping("/search/parkinginfo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MapVertexResponse[].class)
    })
    public ResponseEntity<List<MapVertexInfo>> searchParkingInfo(@ApiParam(value = "关键词", example = "图书馆")
                                                         @RequestParam String keyword) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository);
        return new ResponseEntity<>(mapInfoService.searchParkingSimple(keyword),HttpStatus.OK);
    }

    @ApiOperation(value = "校园内停车点查询（包含车辆数）",
            notes = "给定关键词，查询校园内停车点信息，一般用于主页地图")
    @GetMapping("/search/parking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MapVertexResponse[].class)
    })
    public ResponseEntity<List<MapVertexResponse>> searchParking(@ApiParam(value = "关键词", example = "图书馆")
                                                                 @RequestParam String keyword) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository);
        return new ResponseEntity<>(mapInfoService.searchParking(keyword),HttpStatus.OK);
    }

    @ApiOperation(value = "附近的停车点信息（pending）",
            notes = "给定经纬度，查询校园内停车点信息，一般用于主页地图")
    @GetMapping("/nearby/parking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MapVertexResponse[].class)
    })
    public ResponseEntity<List<MapVertexResponse>> nearbyParking(@ApiParam(value = "经度", example = "121.438324171")
                                                                 @RequestParam double lng,
                                                                 @ApiParam(value = "纬度", example = "31.020556617")
                                                                 @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository);
        return new ResponseEntity<>(
                    mapInfoService.nearbyParking
                        (new GeometryFactory().createPoint(new Coordinate(lng,lat))),
                    HttpStatus.OK);
    }



    @ApiOperation(value = "附近的车辆信息（pending）",
            notes = "给定经纬度，查询校园内实时车辆，一般用于主页地图")
    @GetMapping("/nearby/bikes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = HelloBikeInfo[].class)
    })
    public ResponseEntity<List<HelloBikeInfo>> nearbyBikes(@ApiParam(value = "经度", example = "121.438324171")
                                                               @RequestParam double lng,
                                                           @ApiParam(value = "纬度", example = "31.020556617")
                                                               @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository);
        System.out.println(mapInfoService.nearbyBikes(lng,lat));
        return new ResponseEntity<>(
                mapInfoService.nearbyBikes(lng,lat),
                HttpStatus.OK);
    }
}
