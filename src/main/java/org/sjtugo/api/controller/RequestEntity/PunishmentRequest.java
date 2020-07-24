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

@ApiModel(value = "行程惩罚输入记录")
@Data
public class PunishmentRequest {
    @ApiModelProperty(value = "行程开始记录时间", required = true,
            example = "")
    private int beginRecordTime;
    @ApiModelProperty(value = "路线开始记录时间", required = true,
            example = "")
    private int beginRouteTime;
    @ApiModelProperty(value = "经过路线",
            example = "[[121.33416748046875, 31.156105041503906, 0, 298],[121.33415985107422, 31.156110763549805, 0, 299]]")
    private List<List<Double>>punishlist;
    @ApiModelProperty(value = "类型，0是单车，1是筋斗云，2是汽车",
            example = "1")
    private int Type;
/*
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime departTime = LocalDateTime.now();*/
}
