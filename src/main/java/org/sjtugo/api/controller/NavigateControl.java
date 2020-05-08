package org.sjtugo.api.controller;

import java.util.Collections;
import java.util.Map;

import org.sjtugo.api.entity.NavigateRequest;
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

    @ApiOperation(value = "Main Navigate Service",
            notes = "给定校园内地点ID或经纬度，返回校园巴士出行方案")
    @PostMapping("/bus")
    public EntityModel<Strategy> navigateBus(@RequestBody NavigateRequest navigateRequest) {
        BusPlanner planner = new BusPlanner();
        String[] passPlaces =new String[navigateRequest.getPassPlaceIDs().size()];
        Strategy result = planner.planAll(navigateRequest.getBeginPlaceID(),
                navigateRequest.getPassPlaceIDs().toArray(passPlaces),
                navigateRequest.getArrivePlaceID());
        return new EntityModel<>(result,
                linkTo(methodOn(NavigateControl.class).navigateBus(navigateRequest)).withSelfRel());
    }


}
