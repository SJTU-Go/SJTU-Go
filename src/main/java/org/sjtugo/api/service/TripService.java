package org.sjtugo.api.service;

import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.Trip;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public ResponseEntity<?> startTrip(JSONObject strategy, Integer userID, LocalDateTime departTime){
        Trip trip = new Trip();

        trip.setStrategy(strategy);
        trip.setUserID(userID);
        trip.setDepartTime(departTime);

        Integer difftime = (Integer) strategy.get("travelTime");
        if(difftime==null) {
            return new ResponseEntity<>(new ErrorResponse(5,"travelTime miss"), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime arriveTime = departTime.plusSeconds(difftime);
        trip.setArriveTime(arriveTime);

        tripRepository.save(trip);
        return new ResponseEntity<>(trip.getTripID(), HttpStatus.OK);
    }

    public Optional<Trip> findTrip (Integer tripID) {
        return tripRepository.findById(tripID);
    }
}
