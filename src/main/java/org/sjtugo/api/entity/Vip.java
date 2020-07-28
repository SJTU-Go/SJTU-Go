package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@ApiModel(value = "用户月卡信息")
public class Vip {
    @Id
    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userID;

    @ApiModelProperty(value = "月卡信息（字符串）", example="\"['哈罗单车']\"")
    private String viplist;


    public Vip() {}
}