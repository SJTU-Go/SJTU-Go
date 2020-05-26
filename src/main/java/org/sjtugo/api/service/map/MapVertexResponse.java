package org.sjtugo.api.service.map;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sjtugo.api.DAO.MapVertexInfo;
import org.sjtugo.api.service.MapInfoService;


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
