package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Data
@Entity
@ApiModel("修改记录")
public class Modification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "记录ID",example = "1")
    private Integer modificationID;

    @ApiModelProperty(value = "修改者ID(管理员)", example = "1")
    private Integer adminID;
/*
    @ApiModelProperty(value = "相关交通情况开始时间", example = "07:00")
    @DateTimeFormat(pattern = "HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalTime beginTime;

    @ApiModelProperty(value = "相关交通情况结束时间",example = "08:00")
    @DateTimeFormat(pattern = "HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalTime endTime;
*/
    @ApiModelProperty(value = "备注")
    private String contents;

    @ApiModelProperty(value = "推荐指数",notes = "范围1~5")
    private int star;

    public Modification() {}
}
