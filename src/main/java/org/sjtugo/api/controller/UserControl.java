package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.entity.User;
import org.sjtugo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(value="UserInfo System")
@RestController
@RequestMapping("/user")
public class UserControl {
    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "用户登陆")
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestParam String code){
        UserService userser = new UserService(userRepository);
        return userser.userLogin(code);
    }

}
