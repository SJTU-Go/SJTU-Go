package org.sjtugo.api.service;

import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.Trip;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Integer startTrip(JSONObject strategy, Integer userID){
        Trip trip = new Trip();

        trip.setStrategy(strategy);
        trip.setUserID(userID);
        trip.setDepartTime(LocalDateTime.now());

        Integer difftime = (Integer) strategy.get("travelTime");
        LocalDateTime arriveTime = LocalDateTime.now().plusSeconds(difftime);
        trip.setArriveTime(arriveTime);

        tripRepository.save(trip);
        return trip.getTripID();
    }
}
