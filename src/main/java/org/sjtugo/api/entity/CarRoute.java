package org.sjtugo.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CarRoute extends Route{
    public CarRoute () {
        this.type = RouteType.E100;
    }

    private String departID;

    private String arriveID;

    private int distance;

    private int cost;
}
