package org.sjtugo.api.service;

import org.sjtugo.api.DAO.ModificationRepository;
import org.sjtugo.api.entity.ErrorResponse;
import org.sjtugo.api.entity.Modification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ModificationService {
    private ModificationRepository modificationRepository;

    public ModificationService(ModificationRepository modificationRepository) {
        this.modificationRepository = modificationRepository;
    }

    public List<Modification> getModification(Integer adminID) {
        return modificationRepository.findAll();
    }

    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID, Integer placeID,
                                                   String message, int parkSize) {
        return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);
    }

    public ResponseEntity<ErrorResponse> modifyMap(Integer adminID) {
        return new ResponseEntity<>(new ErrorResponse(0,"修改成功！"), HttpStatus.OK);
    }
}
