package org.sjtugo.api.entity;


import lombok.Data;

import java.time.Duration;

@Data
public class Strategy {
    private String type;

    private String depart;

    private String arrive;

    private String[] pass;

    private Duration travelTime;

    private int distance;

    private int cost;

    private String[] preference;

    private Route[] routeplan;
}
