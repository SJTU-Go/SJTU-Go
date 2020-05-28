package org.sjtugo.api.controller.RequestEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ApiModel(value = "导航输入数据")
@Data
public class NavigateRequest {
    @ApiModelProperty(value = "出发点ID或经纬度", required = true,
            example = "图书馆")
    private String beginPlace;
    @ApiModelProperty(value = "到达点ID或经纬度", required = true,
            example = "POINT (121.435505 31.026303)")
    private String arrivePlace;
    @ApiModelProperty(value = "途径点ID或经纬度",
            example = "[\"学生服务中心\"]")
    private List<String> passPlaces = Collections.emptyList();


    @ApiModelProperty(value = "出发时间（必须严格按照格式，不足位用0补齐），可不设该字段，默认为当前时间",
            example = "2020/05/11 12:05:12")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime departTime = LocalDateTime.now();
}
