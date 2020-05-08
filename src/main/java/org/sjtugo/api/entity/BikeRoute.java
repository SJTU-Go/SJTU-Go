package org.sjtugo.api.entity;

import lombok.Data;

@Data
public class BikeRoute extends Route {
    private String departID;

    private String arriveID;

    private int distance;

    private int cost;

    private String method;
}
