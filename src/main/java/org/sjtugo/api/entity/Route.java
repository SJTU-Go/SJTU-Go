package org.sjtugo.api.entity;

import lombok.Data;
import com.vividsolutions.jts.geom.LineString;

@Data
public abstract class Route {
    private int routeTime;

    private String departName;

    private String arriveName;

    private LineString routePath;
}
