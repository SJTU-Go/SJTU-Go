package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    @ApiModelProperty(value = "修改内容")
    private String contens;

    public Modification() {}
}
