package org.sjtugo.api.DAO.Entity;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="hello_bike_info")
public class HelloBikeInfo {
    @Id
    @Column(name = "bike_id")
    @ApiModelProperty(value = "bikeID", example = "2100003686")
    private String bikeID;

    @Column(name="time")
    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty(value = "爬取时间", example = "18:56:34")
    private LocalDateTime time;

    @Column(name="cluster_point")
    @ApiModelProperty(value = "停车点ID，若为0表示不在任何停车点附近", example = "175196")
    private String clusterPoint;

//    @Transient
//    @JsonSerialize(using = GeometrySerializer.class)
//    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
//    private Point location;

    @Column(name="bike_type")
    @ApiModelProperty(value = "biketype", example= "2")
    private int biketype;

    @Column(name="longitude")
    private double lng;

    @Column(name="latitude")
    private double lat;
//    public Point getLocation(){
//        return new GeometryFactory().createPoint(new Coordinate(lng,lat));


//    }

    public HelloBikeInfo(){

    }

    public HelloBikeInfo(String bikeID, double lng, double lat, int bikeType){
        this.bikeID = bikeID;
        this.lat = lat;
        this.lng = lng;
        this.biketype = bikeType;
        this.time = LocalDateTime.now();
    }


}
