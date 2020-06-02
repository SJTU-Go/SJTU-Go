package org.sjtugo.api.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@ApiModel(value = "历史信息")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "历史ID", example = "123")
    private Integer historyID;

    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userID;

    @ApiModelProperty(value = "出发地", example="上院")
    private String depart;

    @ApiModelProperty(value = "目的地", example="北区篮球场")
    private String arrive;

    @ApiModelProperty(value = "行程时间", example="1140")
    private String routetime;

    @ApiModelProperty(value = "路径")
    private String route;

    public History() {}
}
