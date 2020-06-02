package org.sjtugo.api.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="Traffic Management System")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/traffic")
public class TrafficControl {

}