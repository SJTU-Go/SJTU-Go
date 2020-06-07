package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ApiModel(value = "交通信息")
@Table(name = "traffic_info")
public class TrafficInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Integer trafficID;

    @ApiModelProperty(value = "相关交通情况开始时间，必须是未来时间", example = "07:00")
    @DateTimeFormat(pattern = "HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalTime beginTime;

    @ApiModelProperty(value = "相关交通情况结束时间，必须是未来时间",example = "08:00")
    @DateTimeFormat(pattern = "HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalTime endTime;

    @ApiModelProperty(value = "相关交通情况日期，必须是未来时间",example = "2020/06/30")
    @DateTimeFormat(pattern = "yyyy/MM/dd")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")  //输出参数格式化
    private LocalDate beginDay;

    @ApiModelProperty(value = "交通状况概要信息，用于数据库管理",example = "X路段X时")
    private String name;

    @ApiModelProperty(value = "交通状况具体信息，用于前端展示",example = "XXX路段XXX时刻由于XXX拥堵")
    private String message;

    @ApiModelProperty(value = "电动车通行速度，单位米每秒", example = "8")
    private double motorSpeed;

    @ApiModelProperty(value = "自行车通行速度，单位米每秒", example = "6")
    private double bikeSpeed;

    @ApiModelProperty(value = "汽车通行速度，单位米每秒", example = "16")
    private double carSpeed;

    @ApiModelProperty(value = "提供交通信息的管理者ID", example = "1")
    private Integer adminID;

    @Basic
    @ApiModelProperty(value = "交通路段中的相关地点ID(List Integer)",
            example = "[174061, 140827, 141410, 140826]")
    private String relatedVertex;

    public void setRelatedVertex(List<Integer> vertex) {
        this.relatedVertex = Joiner.on(',').join(vertex);
    }

    public List<Integer> getRelatedVertex() {
        List<Integer> result = new ArrayList<>();
        for (String s : Splitter.on(',').split(this.relatedVertex)) {
            result.add(Integer.valueOf(s));
        }
        return result;
    }

    @ApiModelProperty(value = "重复频率，以天为单位,现阶段功能还未完善，为避免冲突，建议设置成0，" +
            "表示不重复，使得ArangoDB能自动销毁我们的toy task", example = "0")
    private Integer repeatTime;
}
