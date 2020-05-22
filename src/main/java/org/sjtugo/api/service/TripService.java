package org.sjtugo.api.service;

import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.Trip;

import java.time.LocalDateTime;
import java.util.List;

public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Integer startTrip(Strategy strategy, Integer userID){
        Trip trip = new Trip();
        trip.setType(strategy.getType());
        trip.setDepart(strategy.getDepart());
        trip.setArrive(strategy.getArrive());
        trip.setPass(strategy.getPass());
        trip.setCost(strategy.getCost());
        trip.setDistance(strategy.getDistance());
        trip.setPreference(strategy.getPreference());
        //trip.setRouts(strategy.getRouteplan());
        trip.setUserID(userID);
        trip.setDepartTime(LocalDateTime.now());
        //trip.setDepart();
        tripRepository.save(trip);
        return trip.getTripID();
    }
}
