package org.sjtugo.api.entity;

import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "用户反馈信息")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "用户反馈ID",example = "1")
    private String feedbackID;

    @ApiModelProperty(value = "评论者ID", example = "1")
    private String userID;

    @ApiModelProperty(value = "行程ID", example = "1")
    private String tripID;

    @ApiModelProperty(value = "管理员ID", example = "1")
    private String reviewerID;

    @ApiModelProperty(value = "取车反馈", example="1")
    private Integer pickupFB;

    @ApiModelProperty(value = "交通反馈", example="1")
    private Integer trafficFB;

    @ApiModelProperty(value = "停车反馈", example="1")
    private Integer parkFB;

    @ApiModelProperty(value = "服务反馈", example="1")
    private Integer serviceFB;

    @ApiModelProperty(value = "文字反馈内容")
    private String contents;

//    @ApiModelProperty(value = "评论时间", example = "2020/05/11 12:05")
//    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
//    private LocalDateTime commentTime;

    public Feedback() {}
}
