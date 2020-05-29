package org.sjtugo.api.service;

import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Admin;
import org.sjtugo.api.entity.Feedback;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

public class AdminService {
    private final AdminRepository adminRepository;
    private final FeedbackRepository feedbackRepository;

    public AdminService(AdminRepository adminRepository, FeedbackRepository feedbackRepository) {
        this.adminRepository = adminRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public ResponseEntity<ErrorResponse> login(String name, String pw) {
        //检查用户名或密码是否为空 前端?

        Admin admin = adminRepository.findByName(name);
        //判断是否存在
        if (StringUtils.isEmpty(admin)) {
            return new ResponseEntity<>(new ErrorResponse(0, "no such administrator!"), HttpStatus.BAD_REQUEST);
        } else {
            if (admin.getPassword().equals(pw))
                return new ResponseEntity<>(new ErrorResponse(0, "login successfully!"), HttpStatus.OK);
            return new ResponseEntity<>(new ErrorResponse(0, "password error!"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> inbox(Integer adminID) {
        return new ResponseEntity<>(feedbackRepository.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> processFeedback(Integer feedBackID) {
        return new ResponseEntity<>(feedbackRepository.findAll(),HttpStatus.OK);
    }
}
