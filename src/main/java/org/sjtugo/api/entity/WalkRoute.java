package org.sjtugo.api.entity;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

@Data
public class WalkRoute {
    private Point departLocation;

    private Point arriveLocation;

    private int distance;
}
