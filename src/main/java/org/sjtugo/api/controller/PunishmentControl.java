package org.sjtugo.api.controller;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import io.swagger.annotations.*;
import lombok.Data;
import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.CarInfoRepository;
import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.entity.Punishment;
import org.sjtugo.api.DAO.PunishmentRepository;
import org.sjtugo.api.DAO.Entity.CarInfo;
import org.sjtugo.api.DAO.Entity.Destination;
import org.sjtugo.api.DAO.Entity.HelloBikeInfo;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.HelloBikeRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.service.MapInfoService;
import org.sjtugo.api.service.PunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    @ApiOperation(value = "记录惩罚")
    @PostMapping("/punish")
    public ResponseEntity<?> punishmentadd(@RequestBody PunishmentRequest punishmentRequest){
        PunishmentService punisher = new PunishmentService(mapVertexInfoRepository,punishmentRepository);
        return punisher.addpunishment(punishmentRequest.getPunishment());
    }
    @Data
    static class PunishmentRequest{
        @ApiModelProperty(value = "传入列表", example="[ [121.33415985107422, 31.156110763549805, 0, 299], [121.33418273925781, 31.156063079833984, 0, 293]]")
        private List<List<Double>> punishment;
    }


}
