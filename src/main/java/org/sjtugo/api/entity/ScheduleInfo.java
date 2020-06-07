package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@ApiModel(value = "用户日程信息")
public class ScheduleInfo {
    @Id
    @ApiModelProperty(value = "用户ID",example = "123")
    private Integer userID;

    @ApiModelProperty(value = "用户schedule信息",notes = "String保存的列表信息")
    private String schedule;

    public ScheduleInfo() {}
}
