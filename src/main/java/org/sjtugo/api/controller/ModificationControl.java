package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import org.sjtugo.api.DAO.Entity.CarInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.ModificationRepository;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.controller.ResponseEntity.MapVertexResponse;
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
    public ResponseEntity<ErrorResponse> modifyMap(@RequestBody ModifyRequest modifyRequest) {
        ModificationService modiService = new ModificationService(modificationRepository,mapVertexInfoRepository);
        return modiService.modifyMap(modifyRequest.getAdminID(),
                modifyRequest.getPlaceID(),
                modifyRequest.getMessage(),
                modifyRequest.getParkSize());
    }

    @ApiOperation(value = "管理员更新系统")
    @PostMapping("/modify/traffic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "[3]格式错误\n[5]备份失败", response = ErrorResponse.class)
    })
    public ResponseEntity<ErrorResponse> modifyMap(@RequestParam Integer adminID, @RequestBody TrafficInfo trafficInfo) {
        ModificationService modiService = new ModificationService(modificationRepository,mapVertexInfoRepository);
        TrafficService trafficService = new TrafficService(restTemplate, trafficInfoRepository, mapVertexInfoRepository);
        ErrorResponse modiResponse = trafficService.newTraffic(trafficInfo);
        if (modiResponse.getCode() == 0)
        {
            return modiService.modifyMap(adminID, trafficInfo);
        } else {
            return new ResponseEntity<>(modiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @Data
    static class ModifyRequest {
        private Integer adminID;
        private Integer placeID;
        private String message;
        private Integer parkSize;
    }
}
