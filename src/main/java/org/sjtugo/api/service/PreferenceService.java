package org.sjtugo.api.service;

import org.sjtugo.api.DAO.PreferenceRepository;
import org.sjtugo.api.entity.Preference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public class PreferenceService {
    private final PreferenceRepository preferenceRepository;

    public PreferenceService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public ResponseEntity<?> addPreference(Integer userID, String preferencelist, String banlist){
        Preference newPreference = new Preference();
        newPreference.setUserID(userID);
        newPreference.setPreferencelist(preferencelist);
        newPreference.setBanlist(banlist);
        preferenceRepository.save(newPreference);
        return new ResponseEntity<>(newPreference, HttpStatus.OK);
    }

    public Optional<Preference> getPreference(Integer userid){
        return preferenceRepository.findById(userid);
    }
}
