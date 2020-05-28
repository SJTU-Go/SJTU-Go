package org.sjtugo.api.service.NavigateService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import lombok.Data;

import java.time.Duration;
import java.util.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalkResponse {
    private int distance;

    private Duration time;

    private LineString route;

    private Integer status;

    private String message;

    @SuppressWarnings("unchecked")
    @JsonProperty("result")
    private void unpackNested(Map<String,Object> result) {
        ArrayList<Map<String, Object>> routes;
        try {
            routes = (ArrayList<Map<String, Object>>) result.get("routes");
        } catch (Exception e) {
            throw new StrategyNotFoundException("Walk Planner Service deprecated");
        }
        Map<String,Object> route = routes.get(0);
        this.distance = (int) route.get("distance");
        this.time = Duration.ofMinutes((int) route.get("duration"));
        ArrayList<Object> coors = (ArrayList<Object>) route.get("polyline");

        for (var i = 2; i < coors.size() ; i++){
            coors.set(i, (Double) coors.get(i - 2) + ((double) (int) coors.get(i)) / 1000000);
        }
        Coordinate[] coordinates = new Coordinate[coors.size()/2];
        for (var i = 0; i < coors.size()/2 ; i++){
            coordinates[i] = new Coordinate((Double) coors.get(2 * i + 1), (Double) coors.get(2 * i));
        }
        this.route = new GeometryFactory().createLineString(coordinates);

    }
}
