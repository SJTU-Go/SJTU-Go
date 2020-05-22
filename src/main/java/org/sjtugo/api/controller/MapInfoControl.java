package org.sjtugo.api.controller;


import io.swagger.annotations.*;
import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.DAO.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.entity.ErrorResponse;
import org.sjtugo.api.entity.Strategy;
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


    @ApiOperation(value = "地点查询",
            notes = "给定关键词，查询校园内建筑物和停车点")
    @GetMapping("/search")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = MapVertexInfo.class)
    })
    public ResponseEntity<?> searchPlace(@ApiParam(value = "关键词", example = "图书馆")
                                         @RequestParam String keyword) {
        List<Object> result = mapVertexInfoRepository.findByVertexNameLike("%"+keyword+"%");
        result.addAll(destinationRepository.findByPlaceNameLike("%"+keyword+"%"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
