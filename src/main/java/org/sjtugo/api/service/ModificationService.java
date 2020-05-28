package org.sjtugo.api.service;

import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.ModificationRepository;

import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Modification;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

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

    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID, Integer placeID,
                                                   String message, Integer parkSize) { //String??
        //通过placeID找到对应place修改parkSize的信息？？？  管理员手动更改？？？
        MapVertexInfo mapVertexInfo = mapVertexInfoRepository.findById(placeID).orElse(null);
        assert mapVertexInfo != null;
        mapVertexInfo.setParkSize(parkSize);
        mapVertexInfo.setParkInfo(message);
        mapVertexInfoRepository.save(mapVertexInfo);

        //Modification modify = new Modification();
        //Modification modify = modificationRepository.findById(adminID).orElse(null);
        //modify.setAdminID(adminID);
        //modify.setContens(message);
        //modificationRepository.save(modify);

        return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);
    }

    //待修改
    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID, TrafficInfo trafficInfo) {
        Modification modify = new Modification();
        modify.setAdminID(adminID);
        modify.setContens(trafficInfo.toString());
        modificationRepository.save(modify);
        return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);
    }
}
