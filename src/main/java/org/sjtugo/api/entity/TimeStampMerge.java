package org.sjtugo.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@ApiModel
public class TimeStampMerge {
    @Id
    @ApiModelProperty(value = "时间差",example = "11")
    private Integer stampgap;

    @ApiModelProperty(value = "地图点1", example = "123111")
    private Integer vertexid1;

    @ApiModelProperty(value = "地图点2", example = "123111")
    private Integer vertexid2;
    public TimeStampMerge() {}
}
