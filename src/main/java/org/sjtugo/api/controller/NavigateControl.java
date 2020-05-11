package org.sjtugo.api.controller;

import java.util.List;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import lombok.Data;
import org.sjtugo.api.DAO.BusStop;
import org.sjtugo.api.DAO.BusStopRepository;
import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.entity.Strategy;
import io.swagger.annotations.*;
import org.sjtugo.api.service.planner.BusPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api(value="Navigate System")
@RestController
@RequestMapping("/navigate")
public class NavigateControl {
    @Autowired
    private BusStopRepository busStopRepository;
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    @Autowired
    private RestTemplate restTemplate;





    @ApiOperation(value = "Bus Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回校园巴士出行方案")
    @PostMapping("/bus")
    public Strategy navigateBus(@RequestBody NavigateRequest navigateRequest) {
        BusPlanner planner = new BusPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate);
        String[] passPlaces =new String[navigateRequest.getPassPlaces().size()];
        return planner.planAll(navigateRequest.getBeginPlace(),
                navigateRequest.getPassPlaces().toArray(passPlaces),
                navigateRequest.getArrivePlace());
    }

    @ApiOperation(value = "Parse Place",
            notes = "给定校园内地点ID或经纬度，返回地点信息")
    @PostMapping(value = "/parsePlace", produces="text/plain;charset=UTF-8")
    public String parsePlace(@RequestBody NavigateRequest navigateRequest) {
        BusPlanner planner = new BusPlanner(mapVertexInfoRepository,destinationRepository,
                restTemplate);
        return planner.parsePlace(navigateRequest.getBeginPlace()).toString();
    }

    @PostMapping(path="/bus/addRecord")
    public @ResponseBody String addNewBusStop (@RequestParam String stopName
            , @RequestParam String stopLoc) throws ParseException {
        BusStop n = new BusStop();
        n.setStopName(stopName);
        n.setLocation((Point) new WKTReader().read(stopLoc));
        busStopRepository.save(n);
        return "Saved";
    }

    @ApiModel(value = "导航输入数据")
    @Data
    static class NavigateRequest {
        @ApiModelProperty(value = "出发点ID或经纬度", required = true,
                example = "VT000341")
        private String beginPlace;
        @ApiModelProperty(value = "到达点ID或经纬度", required = true,
                example = "121.435624,31.234566")
        private String arrivePlace;
        @ApiModelProperty(value = "途径点ID或经纬度",
                example = "[VT000341, (121.435624;31.234566)]")
        private List<String> passPlaces;
    }
}
