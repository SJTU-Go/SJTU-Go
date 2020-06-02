package org.sjtugo.api.entity;

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

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@ApiModel(value = "评论详情")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "评论ID",example = "1")
    private Integer commentID;

    @ApiModelProperty(value = "评论者ID", example = "1")
    private Integer userID;

    @ApiModelProperty(value = "评论名", example="评论一")
    private String title;

    @ApiModelProperty(value = "评论内容")
    private String contents;

    @ApiModelProperty(value = "评论时间", example = "2020/05/11 12:05")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime commentTime;

    @ElementCollection
    @ApiModelProperty(value = "点赞用户ID", example = "[2,3]")
    private List<Integer> approveUsers;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @ApiModelProperty(value = "评论地点")
    private Point location;

    @ApiModelProperty(value = "评论相关停车点ID", example = "134234")
    private Integer relatedPlace;

    @ApiModelProperty(value = "评论相关停车点名")
    private String parkingName;

    @ElementCollection
    @ApiModelProperty(value = "评论下方的子评论ID", example = "[2,3]")
    private List<Integer> subComment;

    public Comment() {}
}