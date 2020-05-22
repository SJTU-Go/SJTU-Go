package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table
@ApiModel(value = "一次出行信息")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "出行ID",example = "1")
    private Integer tripID;

    @ApiModelProperty(value = "出行者ID", example = "1")
    private Integer userID;

//    @ApiModelProperty()
 //   private Strategy strategy;

    @ApiModelProperty(value = "交通方式", example = "哈罗单车")
    private String type;

    @ApiModelProperty(value = "出发点名称", example = "三餐")
    private String depart;

    @ApiModelProperty(value = "到达地点", example = "上院")
    private String arrive;

    @ElementCollection
    @ApiModelProperty(value = "途径点", example = "[光彪楼, balabala]")
    private List<String> pass;


    @ApiModelProperty(value = "方案总距离，单位为米", example = "589")
    private int distance;

    @ApiModelProperty(value = "方案总花费，单位为分", example = "150")
    private int cost;

    @ElementCollection
    @ApiModelProperty(value = "用户查询时提交的个性化选项", example = "[避开拥堵, 允许禁停区]")
    private List<String> preference;


 /*   @OneToMany(targetEntity = Route.class,fetch = FetchType.LAZY)
    @ApiModelProperty(value = "行程详细方案列表")
    private List<Route> routs;
*/
    @ApiModelProperty(value = "出发时间", example = "2020/05/11 07:36")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime departTime;

    @ApiModelProperty(value = "到达时间", example = "2020/05/11 07:44")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime arriveTime;


    @ApiModelProperty(value = "行程反馈ID")
    private Integer tripFeedback;

    public Trip() {}
}
