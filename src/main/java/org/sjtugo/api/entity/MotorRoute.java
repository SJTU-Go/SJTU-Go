package org.sjtugo.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MotorRoute extends Route{
    public MotorRoute() {
        this.type = RouteType.CLOUDMOTOR;
        this.distance = 0;
    }

    private String departID;

    private String arriveID;

    private int cost;

    private List<String> passingVertex;

    private int driveDistance;
}
