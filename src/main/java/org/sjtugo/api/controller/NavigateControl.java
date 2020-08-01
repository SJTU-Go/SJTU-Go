package org.sjtugo.api.controller;

import java.util.NoSuchElementException;

import com.vividsolutions.jts.io.ParseException;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.controller.RequestEntity.NavigateRequest;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Strategy;
import io.swagger.annotations.*;
import org.sjtugo.api.service.NavigateService.PlaceNotFoundException;
import org.sjtugo.api.service.NavigateService.StrategyNotFoundException;
import org.sjtugo.api.service.NavigateService.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api(value="Navigate System")
@RestController
@RequestMapping("/navigate")
public class NavigateControl {
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private BusTimeRepository busTimeRepository;
    @Autowired
    private BusStopRepository busStopRepository;
    @Autowired
    private VertexDestinationRepository vertexDestinationRepository;
    @Autowired
    private CarInfoRepository carInfoRepository;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private MotorForbidAreaRepository motorForbidAreaRepository;

    @ApiOperation(value = "Walk Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回步行方案")
    @PostMapping("/walk")
    public ResponseEntity<?> navigateWalk(@RequestBody NavigateRequest navigateRequest) {
        WalkPlanner planner = new WalkPlanner(mapVertexInfoRepository,destinationRepository, restTemplate);
        try {
            return new ResponseEntity<>(planner.planAll(navigateRequest), HttpStatus.OK);
        } catch (StrategyNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(4,"Walk Strategy Not Supported"),
                    HttpStatus.NOT_FOUND);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(3,"Place Not Found"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "Bus Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回校园巴士出行方案")
    @PostMapping("/bus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Strategy.class),
            @ApiResponse(code = 404, message = "[5]No need to take Bus", response = ErrorResponse.class)
        })
    public ResponseEntity<?> navigateBus(@RequestBody NavigateRequest navigateRequest) {
        BusPlanner planner = new BusPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate, busTimeRepository,busStopRepository, vertexDestinationRepository);
        try {
            return new ResponseEntity<>(planner.planAll(navigateRequest), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new ErrorResponse(5,"No need to take Bus"),
                    HttpStatus.NOT_FOUND);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(3,"Place Not Found"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Bike Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回哈罗单车出行方案")
    @PostMapping("/bike")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Strategy.class),
            @ApiResponse(code = 404, message = "[3]Place Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> navigateBike(@RequestBody NavigateRequest navigateRequest) {
        BikePlanner planner = new BikePlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate, busTimeRepository,busStopRepository, vertexDestinationRepository);
        try {
            return new ResponseEntity<>(planner.planAll(navigateRequest), HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(3,"Place Not Found"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Motor Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回哈罗单车出行方案")
    @PostMapping("/jindouyun")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Strategy.class),
            @ApiResponse(code = 404, message = "[3]Place Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> navigateMotor(@RequestBody NavigateRequest navigateRequest) {
        JindouyunPlanner planner = new JindouyunPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate, busTimeRepository,busStopRepository,vertexDestinationRepository,motorForbidAreaRepository);
        try {
            return new ResponseEntity<>(planner.planAll(navigateRequest), HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(3,"Place Not Found"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Car Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回旋风E100出行方案")
    @PostMapping("/car")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Strategy.class),
            @ApiResponse(code = 404, message = "[3]Place Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> navigateCar(@RequestBody NavigateRequest navigateRequest) {
        CarPlanner planner = new CarPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate, busTimeRepository,busStopRepository,vertexDestinationRepository, carInfoRepository);
        try {
            return new ResponseEntity<>(planner.planAll(navigateRequest), HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(3,"Place Not Found"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Parse Place",
            notes = "给定校园内地点ID或经纬度，返回地点信息。要求地点的格式：“VT+NUMID”,”DT+NUMID“," +
                    "”POINT(经度 纬度)“，若不满足以上格式，将会被当做搜索关键词，通过腾讯地图API在交大校园" +
                    "内搜索相关地点，匹配地名、经纬度。")
    @PostMapping(value = "/parsePlace", produces="text/plain;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> processPlace(@RequestBody String place) {
        BusPlanner planner = new BusPlanner(mapVertexInfoRepository, destinationRepository,
                restTemplate, busTimeRepository, busStopRepository, vertexDestinationRepository);
        try {
            return new ResponseEntity<>(planner.parsePlace(place).toString(),HttpStatus.OK);
        } catch (PlaceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(3, "Place Not Found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "开始搜索",notes = "输入参数格式与导航相同")
    @PostMapping("/startnavigation")
    public ResponseEntity<?> startNavigation(@RequestBody NavigateRequest navigateRequest) throws ParseException {
        SearchHistory searchHistory = new SearchHistory(searchHistoryRepository,mapVertexInfoRepository);
        return searchHistory.startNavigation(navigateRequest);
    }
//    @PostMapping(path="/bus/addRecord")
//    public @ResponseBody String addNewBusStop (@RequestParam String stopName
//            , @RequestParam String stopLoc) throws ParseException {
//        BusStop n = new BusStop();
//        n.setStopName(stopName);
//        n.setLocation((Point) new WKTReader().read(stopLoc));
//        busStopRepository.save(n);
//        return "Saved";
//    }
}

