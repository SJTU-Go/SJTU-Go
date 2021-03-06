package org.sjtugo.api.controller;

import java.util.Collections;
import java.util.Map;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value="Home System")
@RestController
public class HelloControl {

    @ApiOperation(value = "Hello World",
            notes = "A simple example")
    @GetMapping(value = "/hello", produces="text/plain;charset=UTF-8")
    public String greeting() {
        return "Welcome, This is SJTU-Go Public API, 交大共享出行导航服务";
    }

}