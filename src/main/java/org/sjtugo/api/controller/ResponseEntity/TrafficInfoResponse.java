package org.sjtugo.api.controller.ResponseEntity;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.List;

@Data
@ApiModel(value = "交通信息")
public class TrafficInfoResponse {

    @ApiModelProperty(value = "交通路段中的相关地点坐标")
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    private LineString pointList;

    private TrafficInfo trafficInfo;

    public TrafficInfoResponse(){
    }
}
