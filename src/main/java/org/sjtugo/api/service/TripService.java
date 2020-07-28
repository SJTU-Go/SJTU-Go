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

    public ResponseEntity<?> startTrip(JSONObject strategy, Integer userID){
        Trip trip = new Trip();

        trip.setStrategy(strategy);
        trip.setUserID(userID);
        trip.setDepartTime(LocalDateTime.now());

        Integer difftime = (Integer) strategy.get("travelTime");
        if(difftime==null) {
            return new ResponseEntity<>(new ErrorResponse(5,"travelTime miss"), HttpStatus.BAD_REQUEST);
        }
        LocalDateTime arriveTime = LocalDateTime.now().plusSeconds(difftime);
        trip.setArriveTime(arriveTime);

        trip.setStatus(Trip.TripStatus.STARTED);
        tripRepository.save(trip);
        return new ResponseEntity<>(trip.getTripID(), HttpStatus.OK);
    }

    public Optional<Trip> findTrip (Integer tripID) {
        return tripRepository.findById(tripID);
    }

    public Optional<Trip> cancelTrip (Integer tripID) {
        return tripRepository.findById(tripID)
                .map(trip -> {
                    trip.setStatus(Trip.TripStatus.CANCELLED);
                    return Optional.of(tripRepository.save(trip));
                })
                .orElseGet(Optional::empty);
    }

    public Optional<Trip> failTrip (Integer tripID) {
        return tripRepository.findById(tripID)
                .map(trip -> {
                    trip.setStatus(Trip.TripStatus.ABORTED);
                    return Optional.of(tripRepository.save(trip));
                })
                .orElseGet(Optional::empty);
    }

    public Optional<Trip> endTrip (Integer tripID) {
        return tripRepository.findById(tripID)
                .map(trip -> {
                    trip.setStatus(Trip.TripStatus.FINISHED);
                    return Optional.of(tripRepository.save(trip));
                })
                .orElseGet(Optional::empty);
    }

    public Optional<Trip> commentedTrip (Integer tripID) {
        return tripRepository.findById(tripID)
                .map(trip -> {
                    trip.setStatus(Trip.TripStatus.COMMENTED);
                    return Optional.of(tripRepository.save(trip));
                })
                .orElseGet(Optional::empty);
    }
}
