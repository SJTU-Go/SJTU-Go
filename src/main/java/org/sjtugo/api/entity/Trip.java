package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

import net.sf.json.JSONObject;

@Data
@Entity
@ApiModel(value = "一次出行信息")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "出行ID",example = "1")
    private Integer tripID;

    @ApiModelProperty(value = "出行者ID", example = "1")
    private Integer userID;

    @ApiModelProperty(value = "trip详细信息",notes = "json格式的String")
    @Column(columnDefinition = "TEXT")
    private String strategy;  //Json


    public JSONObject getStrategy() {
        return JSONObject.fromObject(strategy);
    }

    public void setStrategy(JSONObject obj){
        strategy = obj.toString();
    }
    //JSONObject json = JSONObject.fromObject(strategy);


    @ApiModelProperty(value = "出发时间", example = "2020/05/11 07:36:23")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime departTime;

    @ApiModelProperty(value = "到达时间", example = "2020/05/11 07:44:56")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime arriveTime;


    @ApiModelProperty(value = "行程反馈ID")
    private Integer tripFeedback;

    public Trip() {}
}
