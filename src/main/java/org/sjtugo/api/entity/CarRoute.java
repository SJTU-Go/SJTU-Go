package org.sjtugo.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CarRoute extends Route{
    public CarRoute () {
        this.type = RouteType.E100;
        this.distance = 0;
    }

    private String departID;

    private String arriveID;

    private int cost;

    private List<String> passingVertex;

    private int driveDistance;
}
