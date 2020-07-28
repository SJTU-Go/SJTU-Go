package org.sjtugo.api.controller;


import io.swagger.annotations.*;
import lombok.Data;
import org.sjtugo.api.DAO.PunishmentRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.service.PunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.sjtugo.api.controller.RequestEntity.PunishmentRequest;
import java.util.List;


@Api(value="惩罚记录系统")
@RestController
@RequestMapping("/punishment")
public class PunishmentControl {
    @Autowired
    private PunishmentRepository punishmentRepository;
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TripRepository tripRepository;

    @ApiOperation(value = "记录惩罚")
    @PostMapping("/punish")
    public ResponseEntity<?> punishmentadd(@RequestBody List<PunishmentRequest>punishmentRequest,@RequestParam Integer tripid){
        PunishmentService punisher = new PunishmentService(mapVertexInfoRepository,punishmentRepository, tripRepository, restTemplate);
        //todo:延伸到n个的情况
        return punisher.addpunishment(punishmentRequest.get(0).getPunishlist(),tripid,punishmentRequest.get(0).getBeginRouteTime(),punishmentRequest.get(0).getType(),0);
    }


}