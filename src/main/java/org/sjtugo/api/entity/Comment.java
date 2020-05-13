package org.sjtugo.api.entity;

import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
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
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime commentTime;

    @ElementCollection
    @ApiModelProperty(value = "点赞用户ID", example = "[2,3]")
    private List<Integer> approveUsers;

    @ApiModelProperty(value = "评论地点")
    private Point location;

    @ElementCollection
    @ApiModelProperty(value = "评论相关地点ID", example = "[1,2,3]")
    private List<Integer> relatedPlace;

    @ElementCollection
    @ApiModelProperty(value = "评论下方的子评论ID", example = "[2,3]")
    private List<Integer> subComment;

}