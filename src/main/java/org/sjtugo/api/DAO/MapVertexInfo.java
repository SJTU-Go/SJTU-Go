package org.sjtugo.api.DAO;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
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
    private Integer vertexID;

    @Column(name = "vertex_name")
    private String vertexName;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name = "location")
    @ApiModelProperty(value = "坐标位置",
            example = "{type: Point, coordinates: [121.437689, 31.025735]}")
    private Point location;

    @Column(name="park_info")
    private String parkInfo;

    @Column(name="park_size")
    private String parkSize;
}
