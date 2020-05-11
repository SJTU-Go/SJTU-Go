package org.sjtugo.api.service.planner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonRootName("data")
public class PlaceResponse {
    private String title;

    private Point location;

    @SuppressWarnings("unchecked")
    @JsonProperty("data")
    private void unpackNested(Map<String,Object>[] data) {
        this.title = (String) data[0].get("title");
        Map<String,Double> point = (Map<String,Double>) data[0].get("location");
        this.location = new GeometryFactory().createPoint(
                new Coordinate(point.get("lng"),point.get("lat")));
    }
}

