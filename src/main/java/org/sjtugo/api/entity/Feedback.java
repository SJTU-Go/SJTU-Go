package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@ApiModel(value = "用户反馈信息")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //为实体生成唯一标识主键
    @ApiModelProperty(value = "用户反馈ID",example = "123")
    private Integer feedbackID;

    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userID;

    @ApiModelProperty(value = "行程ID", example = "123")
    private Integer tripID;

    @ApiModelProperty(value = "取车反馈", example="5")
    private Integer pickupFB;

    @ApiModelProperty(value = "交通反馈", example="5")
    private Integer trafficFB;

    @ApiModelProperty(value = "停车反馈", example="5")
    private Integer parkFB;

    @ApiModelProperty(value = "服务反馈", example="5")
    private Integer serviceFB;

    @ApiModelProperty(value = "文字反馈内容")
    private String contents;

    @ApiModelProperty(value = "提交反馈时间", example = "2020/05/11 12:05")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime time;

    @ApiModelProperty(value = "已查看该条评论的管理员ID")
    private Integer adminID;
    public Feedback() {}
}
