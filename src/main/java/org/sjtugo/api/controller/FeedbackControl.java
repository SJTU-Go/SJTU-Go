package org.sjtugo.api.controller;


import io.swagger.annotations.*;
import lombok.Data;
import org.hibernate.cfg.CreateKeySecondPass;
import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Feedback;
import org.sjtugo.api.service.AdminService;
import org.sjtugo.api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value="Feedback System")
@RestController
@RequestMapping("/feedback")
public class FeedbackControl {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private AdminRepository adminRepository;

    @ApiOperation(value = "用户添加行程反馈")
    @PostMapping("/add")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Feedback.class),
            @ApiResponse(code = 404, message = "[2]Invalid Feedback", response = ErrorResponse.class)
    })
    public ResponseEntity<?> addFeedback(@RequestBody FeedbackRequest feedbackRequest){
        FeedbackService feedbackser = new FeedbackService(feedbackRepository,null);
        return feedbackser.addFeedback(feedbackRequest.getUserID(),
                feedbackRequest.getTripID(),
                feedbackRequest.getPickupFB(),
                feedbackRequest.getTrafficFB(),
                feedbackRequest.getParkFB(),
                feedbackRequest.getServiceFB(),
                feedbackRequest.getContents());
    }

    @ApiOperation(value = "管理员收件箱,返回所有反馈")
    @PostMapping("/inbox")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Feedback.class),
            @ApiResponse(code = 500, message = "[5]Invalid Administrator", response = ErrorResponse.class)
    })
    public ResponseEntity<?> inbox(@RequestParam Integer adminID) {
        FeedbackService feedbackser = new FeedbackService(feedbackRepository,adminRepository);
        try {
            return feedbackser.inbox(adminID);
        } catch (Exception e) {
            return  new ResponseEntity<>(new ErrorResponse(500,"No such admin!"), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "管理员查看反馈",notes = "对应feedback中的adminID将会变为查看的管理员ID")
    @PostMapping("/processfeedback")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Feedback.class),
            @ApiResponse(code = 500, message = "[5]Feedback Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<?> processFeedback(@RequestParam Integer feedbackID, @RequestParam Integer adminID) {
        FeedbackService feedbackser = new FeedbackService(feedbackRepository,null);
        try {
            return feedbackser.processFeedback(feedbackID,adminID);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(500,"No such feedback!"),HttpStatus.NOT_FOUND);
        }
    }


    @Data
    static class FeedbackRequest{
        @ApiModelProperty(value = "用户ID", example = "123")
        private Integer userID;

        @ApiModelProperty(value = "行程ID", example = "123")
        private Integer tripID;

        @ApiModelProperty(value = "取车反馈", example="5")
        private Integer pickupFB;

        @ApiModelProperty(value = "交通反馈", example="5")
        private Integer trafficFB;

        @ApiModelProperty(value = "停车反馈", example="5")
        private Integer parkFB;

        @ApiModelProperty(value = "服务反馈", example="5")
        private Integer serviceFB;

        @ApiModelProperty(value = "文字反馈内容")
        private String contents;
    }

}


