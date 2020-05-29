package org.sjtugo.api.service;

import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

public class AdminService {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<ErrorResponse> login(String name, String pw) {
        //检查用户名或密码是否为空 前端?

        Admin admin = adminRepository.findByName(name);
        //判断是否存在
        if (StringUtils.isEmpty(admin)) {
            return new ResponseEntity<>(new ErrorResponse(0, "管理员不存在！"), HttpStatus.BAD_REQUEST);
        } else {
            if (admin.getPassword().equals(pw))
                return new ResponseEntity<>(new ErrorResponse(0, "登陆成功！"), HttpStatus.OK);
            return new ResponseEntity<>(new ErrorResponse(0, "密码错误！"), HttpStatus.BAD_REQUEST);
        }
    }
}
