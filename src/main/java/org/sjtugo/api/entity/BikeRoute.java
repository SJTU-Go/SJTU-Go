package org.sjtugo.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BikeRoute extends Route {
    private String departID;

    private String arriveID;

    private int cost;

    private String method;

    private List<String> passingVertex;

    private int rideDistance;

    public BikeRoute(){
        this.type = RouteType.HELLOBIKE;
        this.distance = 0;
    }
}
