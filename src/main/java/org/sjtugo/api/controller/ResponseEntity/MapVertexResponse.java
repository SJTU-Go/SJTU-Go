package org.sjtugo.api.controller.ResponseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;


@Data
public class MapVertexResponse{
    private MapVertexInfo vertexInfo;

    @ApiModelProperty(value = "哈罗单车数", example = "15")
    private int bikeCount = 0;

    @ApiModelProperty(value = "筋斗云车辆数", example = "0")
    private int motorCount = 0;

    public MapVertexResponse(){

    }
}
