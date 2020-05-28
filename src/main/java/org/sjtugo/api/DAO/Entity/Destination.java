package org.sjtugo.api.DAO.Entity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "destination")
public class Destination {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "placeid")
    @ApiModelProperty(value = "目的地或建筑物ID", example = "137370")
    private Integer placeID;

    @Column(name="place_name")
    @ApiModelProperty(value = "目的地或建筑物名", example = "东大门")
    private String placeName;

    @Column(name="nick_name")
    @ApiModelProperty(value = "别名，用于模糊检索，由管理员维护", example = "紫气东来门 庙门")
    private String nickName;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name="location")
    @ApiModelProperty(value = "坐标位置",
            example = "{type: Point, coordinates: [121.437689, 31.025735]}")
    private Point location;

    @Column(name="place_info")
    @ApiModelProperty(value = "建筑物或目的地信息，由管理员维护", example = "全年畅通")

    private String placeInfo;
}
