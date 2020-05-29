package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.ModificationRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Modification;
import org.sjtugo.api.entity.TrafficInfo;
import org.sjtugo.api.service.ModificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value="修改记录信息")
@RestController
@RequestMapping("/modification")
public class ModificationControl {
    @Autowired
    private ModificationRepository modificationRepository;
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;

    @ApiOperation(value = "管理员查看修改记录")
    @PostMapping("/view")
    public @ResponseBody List<Modification> getModification(@RequestParam Integer adminID) {
        ModificationService modiService = new ModificationService(modificationRepository,null);
        return modiService.getModification(adminID);
    }

    @ApiOperation(value = "管理员更新系统")
    @PostMapping("/modify/park")
    public ResponseEntity<ErrorResponse> modifyMap(@RequestBody ModifyRequest modifyRequest) {
        ModificationService modiService = new ModificationService(modificationRepository,mapVertexInfoRepository);
        return modiService.modifyMap(modifyRequest.getAdminID(),
                modifyRequest.getPlaceID(),
                modifyRequest.getMessage(),
                modifyRequest.getParkSize());
    }

    @ApiOperation(value = "管理员更新系统")
    @PostMapping("/modify/traffic")
    public ResponseEntity<ErrorResponse> modifyMap(@RequestParam Integer adminID, @RequestParam TrafficInfo trafficInfo) {
        ModificationService modiService = new ModificationService(modificationRepository,mapVertexInfoRepository);
        return modiService.modifyMap(adminID, trafficInfo);
    }

    @Data
    static class ModifyRequest {
        private Integer adminID;
        private Integer placeID;
        private String message;
        private Integer parkSize;
    }
}
