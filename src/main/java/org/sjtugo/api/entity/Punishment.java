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
@Table(name = "punishment")
@ApiModel(value = "惩罚信息表格")

public class Punishment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "惩罚ID",example = "1")
    private Integer punishid;

    @ApiModelProperty(value = "记录时间", example = "2020/05/11 07:36:23")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime time;

    @ApiModelProperty(value = "出发点id")
    private Integer vertexDepart;

    @ApiModelProperty(value = "结束点ID")
    private Integer vertexArrive;

    @ApiModelProperty(value = "出行工具类型")
    private Integer type;
    public Punishment() {}
}