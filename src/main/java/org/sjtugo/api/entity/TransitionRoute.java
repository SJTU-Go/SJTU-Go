package org.sjtugo.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransitionRoute extends Route {
    private String placeID;

    private String parkID;

    private boolean isDepart;

    public TransitionRoute(boolean isDepart){
        this.isDepart = isDepart;
        this.type = isDepart ? RouteType.FIND : RouteType.PARK;
        this.distance = 100; // TODO default value
    }
}
