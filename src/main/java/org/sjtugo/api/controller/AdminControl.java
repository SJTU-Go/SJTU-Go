package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sjtugo.api.DAO.AdminRepository;
import org.sjtugo.api.DAO.FeedbackRepository;
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
    @Autowired
    private FeedbackRepository feedbackRepository;

    @ApiOperation(value = "管理员登录",notes = "数据库已有数据：用户名lyuf，密码123456")
    @PostMapping("/adminlogin")
    public ResponseEntity<ErrorResponse> adminLogin(@RequestParam String name, @RequestParam String pw) {
        AdminService adminser = new AdminService(adminRepository,null);
        return adminser.login(name,pw);
    }

    @ApiOperation(value = "管理员收件箱")
    @PostMapping("/inbox")
    public ResponseEntity<?> inbox(@RequestParam Integer adminID) {
        AdminService adminser = new AdminService(adminRepository,feedbackRepository);
        return adminser.inbox(adminID);
    }

    @ApiOperation(value = "查看反馈")
    @PostMapping("/feedback")
    public ResponseEntity<?> processFeedback(@RequestParam Integer feedbackID) {
        AdminService adminser = new AdminService(adminRepository,feedbackRepository);
        return adminser.processFeedback(feedbackID);
    }
}
