package org.sjtugo.api.service;


import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.ModificationRepository;

import org.sjtugo.api.config.JsonDateTimeValueProcessor;
import org.sjtugo.api.config.JsonDateValueProcessor;
import org.sjtugo.api.config.JsonTimeValueProcessor;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Modification;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ModificationService {
    private final ModificationRepository modificationRepository;
    private final MapVertexInfoRepository mapVertexInfoRepository;

    public ModificationService(ModificationRepository modificationRepository,
                               MapVertexInfoRepository mapVertexInfoRepository) {
        this.modificationRepository = modificationRepository;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
    }

    public List<Modification> getModification(Integer adminID) {
        return modificationRepository.findByAdminID(adminID);
    }

    public Optional<Modification> getModificationById(Integer modiID) {
        return modificationRepository.findById(modiID);
    }

    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID, Integer placeID,
                                                   String message, Integer parkSize) {
        MapVertexInfo mapVertexInfo = mapVertexInfoRepository.findById(placeID).orElse(null);
        assert mapVertexInfo != null;
        String placeName;
        try {
            placeName = mapVertexInfo.getVertexName();
            mapVertexInfo.setParkSize(parkSize);
            mapVertexInfo.setParkInfo(message);
            mapVertexInfoRepository.save(mapVertexInfo);

            Modification modify = new Modification();
            modify.setAdminID(adminID);
            modify.setContents(message);
            modify.setPlaceName(placeName);
            modify.setTime(LocalDateTime.now());
            modificationRepository.save(modify);

            return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);
        } catch (Exception e) {
            //System.out.println("no such place");
            return new ResponseEntity<>(new ErrorResponse(5,"no such place！"), HttpStatus.BAD_REQUEST);
        }

    }

    public void modifyMap(Integer adminID, TrafficInfo trafficInfo) {
        Modification modify = new Modification();
//        try{
        modify.setAdminID(adminID);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(LocalDate.class,new JsonDateValueProcessor());
        jsonConfig.registerJsonValueProcessor(LocalDateTime.class,new JsonDateTimeValueProcessor());
        jsonConfig.registerJsonValueProcessor(LocalTime.class,new JsonTimeValueProcessor());
        modify.setContents(JSONObject.fromObject(trafficInfo,jsonConfig).toString());
        modificationRepository.save(modify);
//        } catch (Exception e) {
////            new ErrorResponse(5, "备份失败");
//            return;
//        }
////        new ErrorResponse(0, "修改成功！");
    }

    public void deleteTraffic(Integer id){
        modificationRepository.deleteById(id);
    }

}
