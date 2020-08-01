package org.sjtugo.api.service;

import org.sjtugo.api.DAO.VipRepository;
import org.sjtugo.api.entity.Vip;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class VipService {
    private final VipRepository vipRepository;

    public VipService(VipRepository vipRepository) {
        this.vipRepository = vipRepository;
    }

    public ResponseEntity<?> addVip(Integer userID, String viplist){
        Vip newVip = new Vip();
        newVip.setUserID(userID);
        newVip.setViplist(viplist);

        vipRepository.save(newVip);
        return new ResponseEntity<>(newVip, HttpStatus.OK);
    }

    public Optional<Vip> getVip(Integer userid){
        return vipRepository.findById(userid);
    }
}
