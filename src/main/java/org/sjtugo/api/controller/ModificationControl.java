package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.ModificationRepository;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.controller.RequestEntity.ParkingspotModify;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Modification;
import org.sjtugo.api.entity.TrafficInfo;
import org.sjtugo.api.service.ModificationService;
import org.sjtugo.api.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Api(value="修改记录信息")
@RestController
@RequestMapping("/modification")
public class ModificationControl {
    @Autowired
    private ModificationRepository modificationRepository;
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private TrafficInfoRepository trafficInfoRepository;
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "管理员查看修改记录")
    @PostMapping("/view")
    public @ResponseBody List<Modification> getModification(@RequestParam Integer adminID) {
        ModificationService modiService = new ModificationService(modificationRepository,null);
        return modiService.getModification(adminID);
    }

    @ApiOperation(value = "管理员更新系统")
    @PostMapping("/modify/parking")
    public ResponseEntity<ErrorResponse> modifyMap(@RequestParam Integer adminID, @RequestBody ParkingspotModify modifyRequest) {
        ModificationService modiService = new ModificationService(modificationRepository,mapVertexInfoRepository);
        try {
            return modiService.modifyMap(adminID, modifyRequest);
        }catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(5,"no such place!"), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "管理员更新系统")
    @PostMapping("/modify/traffic")
    public ResponseEntity<ErrorResponse> modifyMap(@RequestParam Integer adminID, @RequestBody TrafficInfo trafficInfo) {
        ModificationService modiService = new ModificationService(modificationRepository,mapVertexInfoRepository);
        TrafficService trafficService = new TrafficService(restTemplate, trafficInfoRepository, mapVertexInfoRepository);
        trafficService.newTraffic(trafficInfo);
        return modiService.modifyMap(adminID, trafficInfo);
    }

//    @Data
//    static class ModifyRequest {
//        private Integer adminID;
//        private Integer placeID;
//        private String message;
//        private Integer parkSize;
//    }
}
