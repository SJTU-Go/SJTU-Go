package org.sjtugo.api.entity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="helloBikeInfo")
public class HelloBikeInfo {
    @Id
    @Column(name = "BikeID")
    @ApiModelProperty(value = "bikeID", example = "")
    private String bikeID;

    @Column(name="time")
    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    private LocalDateTime time;

    @Column(name="cluster_point")
    private String clusterPoint;

//    @Transient
//    @JsonSerialize(using = GeometrySerializer.class)
//    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
//    private Point location;

    @Column(name="bikeType")
    private int bikeType;

    @Column(name="longitude")
    private double lng;

    @Column(name="latitude")
    private double lat;
//    public Point getLocation(){
//        return new GeometryFactory().createPoint(new Coordinate(lng,lat));


//    }
}
