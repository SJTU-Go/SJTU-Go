package org.sjtugo.api.entity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.vividsolutions.jts.geom.LineString;

@Data
public abstract class Route {
    @ApiModelProperty(value = "路段交通方式")
    protected RouteType type;

    @ApiModelProperty(value = "路段用时，秒为单位",
            example = "70")
    protected int routeTime;
    @ApiModelProperty(value = "出发点名称",
            example = "上院215")
    protected String departName;
    @ApiModelProperty(value = "到达点名称",
            example = "上院东侧停车点")
    protected String arriveName;

    @ApiModelProperty(value = "步行距离，米为单位",
            example = "2354")
    protected int distance;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @ApiModelProperty(value = "路线",
            example = "{type: LineString, coordinates: [[121.437689, 31.025735],[ 121.43766600 , 31.025728]]}")
    protected LineString routePath;

    enum RouteType {
        WALK,
        BUS,
        HELLOBIKE,
        MOBIKE,
        E100,
        CLOUDMOTOR,
        FIND,
        PARK
    }
}
