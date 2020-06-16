package org.sjtugo.api.service;

import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Admin;
import org.sjtugo.api.entity.Feedback;
import org.sjtugo.api.entity.Trip;
import org.sjtugo.api.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, AdminRepository adminRepository, UserRepository userRepository, TripRepository tripRepository){
        this.feedbackRepository = feedbackRepository;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }

    public ResponseEntity<?> addFeedback(Integer userID, Integer tripID, Integer pickupFB, Integer trafficFB,
                                         Integer parkFB, Integer serviceFB, String contents){
        if(pickupFB>5 || pickupFB<1 || trafficFB>5 || trafficFB<1 || parkFB>5 || parkFB<1 || serviceFB>5 || serviceFB<1){
            throw new RuntimeException("反馈评分需为1-5的整数");
        }
        Feedback newFeedback = new Feedback();
        newFeedback.setUserID(userID);
        newFeedback.setTripID(tripID);
        newFeedback.setPickupFB(pickupFB);
        newFeedback.setTrafficFB(trafficFB);
        newFeedback.setParkFB(parkFB);
        newFeedback.setServiceFB(serviceFB);
        newFeedback.setContents(contents);
        newFeedback.setTime(LocalDateTime.now());
        newFeedback.setUserName(userRepository.findById(userID).orElse(new User()).getName());
        newFeedback.setBeginPlace((String) tripRepository.findById(tripID).orElse(new Trip()).getStrategy().get("depart"));
        newFeedback.setEndPlace((String) tripRepository.findById(tripID).orElse(new Trip()).getStrategy().get("arrive"));
        feedbackRepository.save(newFeedback);

        return new ResponseEntity<>(newFeedback, HttpStatus.OK);
    }

    public ResponseEntity<?> inbox(Integer adminID) {
        Admin admin = adminRepository.findById(adminID).orElse(null);
        if(admin==null)
            return new ResponseEntity<>(new ErrorResponse(5,"Illegal administrator"),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(feedbackRepository.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> processFeedback(Integer feedBackID,Integer adminID) {
        Feedback feedback = feedbackRepository.findById(feedBackID).orElse(null);
        assert feedback != null;
        try {
            if (feedback.getAdminID()==null)
            {
                feedback.setAdminID(adminID);
                feedbackRepository.save(feedback);
            } //关于save的问题
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(5,"No such feedback!"),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(feedbackRepository.findById(feedBackID),HttpStatus.OK);
    }


//    public ResponseEntity<ErrorResponse> deleteFeedback(Integer feedbackID) {
//        feedbackRepository.deleteByFeedbackID(feedbackID);
//        return new ResponseEntity<>(new ErrorResponse(0,"delete successfully!"),HttpStatus.OK);
//    }
}

