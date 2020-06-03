package org.sjtugo.api.service;

import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.controller.UserControl;
import org.sjtugo.api.entity.Trip;


import java.util.List;

public class HistoryService {
    private final TripRepository tripRepository;

    public HistoryService(TripRepository tripRepository){
        this.tripRepository = tripRepository;
    }

    public List<Trip> getHistoryList(Integer userID){
        //List<Trip> trips = tripRepository.findByUserID(userID);
        return tripRepository.findByUserID(userID)
    }
}

