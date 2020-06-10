package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.DAO.FeedbackRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.controller.ResponseEntity.TrafficInfoResponse;
import org.sjtugo.api.entity.Admin;
import org.sjtugo.api.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "管理员信息")
@RestController
@RequestMapping("/admin")
public class AdminControl {
    @Autowired
    private AdminRepository adminRepository;

    @ApiOperation(value = "管理员登录",notes = "数据库已有数据：用户名lyuf，密码123456")
    @PostMapping("/adminlogin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Integer.class),
            @ApiResponse(code = 404, message = "No Such Administrator", response = ErrorResponse.class)
    })
    public ResponseEntity<?> adminLogin(@RequestParam String name, @RequestParam String pw) {
        AdminService adminser = new AdminService(adminRepository);
        try {
            return adminser.login(name, pw);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(500, "No such admin!"), HttpStatus.NOT_FOUND);
        }
    }
}
