package org.sjtugo.api.service;

import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
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

    public ResponseEntity<?> inbox(Integer adminID) {
        if (adminID!=null)
            return new ResponseEntity<>(feedbackRepository.findAll(),HttpStatus.OK);
        return new ResponseEntity<>(new ErrorResponse(5,"please login"),HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> processFeedback(Integer feedBackID,Integer adminID) {
        Feedback feedback = feedbackRepository.findById(feedBackID).orElse(null);
        assert feedback != null;
        if (feedback.getAdminID()==null)
            feedback.setAdminID(adminID);
        feedbackRepository.save(feedback); //关于save的问题
        return new ResponseEntity<>(feedbackRepository.findById(feedBackID),HttpStatus.OK);
    }


//    public ResponseEntity<ErrorResponse> deleteFeedback(Integer feedbackID) {
//        feedbackRepository.deleteByFeedbackID(feedbackID);
//        return new ResponseEntity<>(new ErrorResponse(0,"delete successfully!"),HttpStatus.OK);
//    }
}

