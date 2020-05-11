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
    @ApiModelProperty(value = "路段用时，秒为单位",
            example = "70")
    protected int routeTime;
    @ApiModelProperty(value = "出发点名称",
            example = "上院215")
    protected String departName;
    @ApiModelProperty(value = "到达点名称",
            example = "上院东侧停车点")
    protected String arriveName;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @ApiModelProperty(value = "路线",
            example = "[[121.23567,31.45678],[121.32123,31.45789]]")
    protected LineString routePath;
}
