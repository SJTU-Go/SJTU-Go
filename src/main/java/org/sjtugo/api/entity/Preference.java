package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@ApiModel(value = "用户偏好排序")
public class Preference {
    @Id
    @ApiModelProperty(value = "用户ID", example = "123")
    private Integer userID;

    @ApiModelProperty(value = "偏好排序（字符串）", example="\"['步行','共享单车','校园巴士']\"")
    private String preferencelist;

    @ApiModelProperty(value = "禁止列表（字符串）", example="\"[]\"")
    private String banlist;

    public Preference() {}
}
