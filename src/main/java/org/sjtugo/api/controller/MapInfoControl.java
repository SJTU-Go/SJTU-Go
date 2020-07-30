package org.sjtugo.api.controller;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import io.swagger.annotations.*;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.DAO.Entity.*;
import org.sjtugo.api.service.MapInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    @Autowired
    private CarInfoRepository carInfoRepository;
    @Autowired
    private JindouyunRepository jindouyunRepository;
    @Autowired
    private RestTemplate restTemplate;


    @ApiOperation(value = "校园内地点查询",
            notes = "给定关键词，查询校园内建筑物信息")
    @GetMapping("/search/destination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Destination[].class)
    })
    public ResponseEntity<List<Destination>> searchPlace(@ApiParam(value = "关键词", example = "图书馆")
                                         @RequestParam String keyword) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                                        destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
        return new ResponseEntity<>(mapInfoService.searchPlace(keyword),HttpStatus.OK);
    }

//    @ApiOperation(value = "校园内停车点查询（不含车辆数）",
//            notes = "给定关键词，查询校园内停车点信息，一般用于搜索框")
//    @GetMapping("/search/parkinginfo")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK", response = MapVertexInfo[].class)
//    })
//    public ResponseEntity<List<MapVertexInfo>> searchParkingInfo(@ApiParam(value = "关键词", example = "图书馆")
//                                                         @RequestParam String keyword) {
//        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
//                destinationRepository,helloBikeRepository, carInfoRepository, restTemplate);
//        return new ResponseEntity<>(mapInfoService.searchParkingSimple(keyword),HttpStatus.OK);
//    }

    @ApiOperation(value = "校园内停车点查询（包含车辆数）",
            notes = "给定关键词，查询校园内停车点信息，一般用于主页地图")
    @GetMapping("/search/parking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MapVertexInfo[].class)
    })
    public ResponseEntity<List<MapVertexInfo>> searchParking(@ApiParam(value = "关键词", example = "图书馆")
                                                                 @RequestParam String keyword) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
        return new ResponseEntity<>(mapInfoService.searchParking(keyword),HttpStatus.OK);
    }

    @ApiOperation(value = "附近的停车点信息",
            notes = "给定经纬度，查询校园内停车点信息，一般用于主页地图")
    @GetMapping("/nearby/parking")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MapVertexInfo[].class)
    })
    public ResponseEntity<List<MapVertexInfo>> nearbyParking(@ApiParam(value = "经度", example = "121.438324171")
                                                                 @RequestParam double lng,
                                                                 @ApiParam(value = "纬度", example = "31.020556617")
                                                                 @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
        return new ResponseEntity<>(
                    mapInfoService.nearbyParking
                        (new GeometryFactory().createPoint(new Coordinate(lng,lat))),
                    HttpStatus.OK);
    }

    @ApiOperation(value = "附近的停车点信息",
            notes = "给定经纬度，查询校园内停车点信息，一般用于主页地图")
    @GetMapping("/nearby/destination")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Destination[].class)
    })
    public ResponseEntity<List<Destination>> nearbyDestination(@ApiParam(value = "经度", example = "121.438324171")
                                                             @RequestParam double lng,
                                                             @ApiParam(value = "纬度", example = "31.020556617")
                                                             @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
        return new ResponseEntity<>(
                mapInfoService.nearbyDestination
                        (new GeometryFactory().createPoint(new Coordinate(lng,lat))),
                HttpStatus.OK);
    }



    @ApiOperation(value = "附近的车辆信息",
            notes = "给定经纬度，查询校园内实时哈啰单车，用于主页地图")
    @GetMapping("/nearby/bikes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = HelloBikeInfo[].class)
    })
    public ResponseEntity<List<HelloBikeInfo>> nearbyBikes(@ApiParam(value = "经度", example = "121.438324171")
                                                               @RequestParam double lng,
                                                           @ApiParam(value = "纬度", example = "31.020556617")
                                                               @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
//        System.out.println(mapInfoService.nearbyBikes(lng,lat));
        return new ResponseEntity<>(
                mapInfoService.nearbyBikes(lng,lat),
                HttpStatus.OK);
    }

    @ApiOperation(value = "附近的车辆信息",
            notes = "给定经纬度，查询校园内实时筋斗云车辆，用于主页地图")
    @GetMapping("/nearby/jindouyun")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = JindouyunInfo[].class)
    })
    public ResponseEntity<List<JindouyunInfo>> nearbyElectroMobiles(@ApiParam(value = "经度", example = "121.438324171")
                                                           @RequestParam double lng,
                                                                   @ApiParam(value = "纬度", example = "31.020556617")
                                                           @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
//        System.out.println(mapInfoService.nearbyElectroMobiles(lng,lat));
        return new ResponseEntity<>(
                mapInfoService.nearbyElectroMobiles(lng,lat),
                HttpStatus.OK);
    }

    @ApiOperation(value = "附近的E100车辆信息",
            notes = "返回所有校园内实时E100车辆，用于主页地图")
    @GetMapping("/nearby/cars")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CarInfo[].class)
    })
    public ResponseEntity<List<CarInfo>> nearbyCars() {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
        return new ResponseEntity<>(
                mapInfoService.nearbyCars(),
                HttpStatus.OK);
    }

    @ApiOperation(value = "附近的摩拜单车信息",
            notes = "实时访问摩拜单车的API，爬取附近的车辆信息（注：结果为临时数据，且ID随机生成）")
    @GetMapping("/nearby/mobike")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = CarInfo[].class)
    })
    public ResponseEntity<List<HelloBikeInfo>> nearbyMobikes(@ApiParam(value = "经度", example = "121.438324171")
                                                                 @RequestParam double lng,
                                                             @ApiParam(value = "纬度", example = "31.020556617")
                                                                 @RequestParam double lat) {
        MapInfoService mapInfoService = new MapInfoService(mapVertexInfoRepository,
                destinationRepository,helloBikeRepository, carInfoRepository, jindouyunRepository, restTemplate);
        return new ResponseEntity<>(
                mapInfoService.nearbyMobikes(lat,lng),
                HttpStatus.OK);
    }

}
