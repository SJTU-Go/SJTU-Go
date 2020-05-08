package org.sjtugo.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;
import lombok.Data;
import org.sjtugo.api.entity.Strategy;
import io.swagger.annotations.*;
import org.sjtugo.api.service.BusPlanner;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Api(value="Navigate System")
@RestController
@RequestMapping("/navigate")
public class NavigateControl {

    @ApiOperation(value = "Bus Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回校园巴士出行方案")
    @PostMapping("/bus")
    public Strategy navigateBus(@RequestBody NavigateRequest navigateRequest) {
        BusPlanner planner = new BusPlanner();
        String[] passPlaces =new String[navigateRequest.getPassPlaces().size()];
        return planner.planAll(navigateRequest.getBeginPlace(),
                navigateRequest.getPassPlaces().toArray(passPlaces),
                navigateRequest.getArrivePlace());
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
