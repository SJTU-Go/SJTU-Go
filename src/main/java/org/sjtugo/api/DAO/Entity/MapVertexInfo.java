package org.sjtugo.api.DAO.Entity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.awt.*;

@Data
@Entity
@Table(name = "map_vertex_info")
public class MapVertexInfo {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="vertexid")
    @ApiModelProperty(value = "地图点ID", example = "141475")
    private Integer vertexID;

    @Column(name = "vertex_name")
    @ApiModelProperty(value = "地图点名称，若有名字则是停车点，若为空则表明是一般的路口", example = "凯旋门（停车点）")
    private String vertexName;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name = "location")
    @ApiModelProperty(value = "坐标位置",
            example = "{type: Point, coordinates: [121.437689, 31.025735]}")
    private Point location;

    @Column(name="park_info")
    @ApiModelProperty(value = "停车点附加信息，由管理员维护" , example = "16:00~17:00拥挤")
    private String parkInfo;

    @Column(name="park_size")
    @ApiModelProperty(value = "停车点最大容量，由管理员维护，用于计算寻车难度和拥堵程度", example = "60")
    private Integer parkSize;

    @Column(name="is_car_vertex")
    @ApiModelProperty(value = "是否允许汽车通过")
    private Boolean isCarVertex;
}
