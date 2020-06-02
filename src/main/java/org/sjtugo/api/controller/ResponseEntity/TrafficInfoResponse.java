package org.sjtugo.api.controller.ResponseEntity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.List;

@Data
@ApiModel(value = "交通信息")
public class TrafficInfoResponse {

    private Integer trafficID;

    @ApiModelProperty(value = "相关交通情况开始时间", example = "07:00")
    @DateTimeFormat(pattern = "HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalTime beginTime;

    @ApiModelProperty(value = "相关交通情况结束时间",example = "08:00")
    @DateTimeFormat(pattern = "HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalTime endTime;

    @ApiModelProperty(value = "交通状况具体信息",example = "拥堵")
    private String message;

    @ApiModelProperty(value = "电动车限速")
    private double motorSpeed;

    @ApiModelProperty(value = "自行车限速")
    private double bikeSpeed;

    @ApiModelProperty(value = "汽车限速")
    private double carSpeed;

    @ApiModelProperty(value = "提供交通信息的管理者ID")
    private Integer adminID;

    @ApiModelProperty(value = "交通路段中的相关地点ID")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    private List<Point> relatedVertex;



}
