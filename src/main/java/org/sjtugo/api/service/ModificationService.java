package org.sjtugo.api.service;

import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.ModificationRepository;

import org.sjtugo.api.controller.RequestEntity.ParkingspotModify;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Modification;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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

    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID, ParkingspotModify modifyRequest) {
        MapVertexInfo mapVertexInfo = mapVertexInfoRepository.findById(modifyRequest.getPlaceID()).orElse(null);
        assert mapVertexInfo != null;
//            String placeName = mapVertexInfo.getVertexName();
        mapVertexInfo.setParkSize(modifyRequest.getParkSize());
        mapVertexInfo.setParkInfo(modifyRequest.getMessage());
        mapVertexInfoRepository.save(mapVertexInfo);

        Modification modify = new Modification();
        modify.setAdminID(adminID);
        modify.setContents(modifyRequest.toString());
//        modify.setPlaceName(placeName);
        modify.setTime(LocalDateTime.now());
        modificationRepository.save(modify);

        return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);

    }

    //待修改
    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID, TrafficInfo trafficInfo) {
        Modification modify = new Modification();
        modify.setAdminID(adminID);
        modify.setContents(trafficInfo.toString());
        modificationRepository.save(modify);
        return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);
    }

}
