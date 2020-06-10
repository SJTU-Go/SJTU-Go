package org.sjtugo.api.controller.RequestEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "停车点修改输入数据")
@Data
public class ParkingspotModify {
    @ApiModelProperty(value = "停车点ID")
    private Integer placeID;
    @ApiModelProperty(value = "备注信息")
    private String message;
    @ApiModelProperty(value = "停车点容量")
    private Integer parkSize;
}
