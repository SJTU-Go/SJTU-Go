package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
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

    @ApiOperation(value = "管理员登录")
    @PostMapping("/adminlogin")
    public ResponseEntity<ErrorResponse> adminLogin(@RequestParam String name, @RequestParam String pw) {
        AdminService adminser = new AdminService(adminRepository);
        return adminser.login(name,pw);
    }
}
