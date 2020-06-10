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

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<?> login(String name, String pw) {
        Admin admin = adminRepository.findByName(name);
        assert admin != null;
        if (admin.getPassword().equals(pw))
            return new ResponseEntity<>(admin.getAdminID(),HttpStatus.OK);
        return new ResponseEntity<>(new ErrorResponse(3, "Password error!"), HttpStatus.BAD_REQUEST);
    }
}
