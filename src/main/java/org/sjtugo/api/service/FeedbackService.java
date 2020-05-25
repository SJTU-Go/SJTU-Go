package org.sjtugo.api.service;

import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.entity.Feedback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository){
        this.feedbackRepository = feedbackRepository;
    }

    public ResponseEntity<?> addFeedback(Integer userID, Integer tripID, Integer pickupFB, Integer trafficFB,
                                         Integer parkFB, Integer serviceFB, String contents){
        Feedback newFeedback = new Feedback();
        newFeedback.setUserID(userID);
        newFeedback.setTripID(tripID);
        newFeedback.setPickupFB(pickupFB);
        newFeedback.setTrafficFB(trafficFB);
        newFeedback.setParkFB(parkFB);
        newFeedback.setServiceFB(serviceFB);
        newFeedback.setContents(contents);
        newFeedback.setTime(LocalDateTime.now());
        feedbackRepository.save(newFeedback);

        return new ResponseEntity<>(newFeedback, HttpStatus.OK);
    }
}

