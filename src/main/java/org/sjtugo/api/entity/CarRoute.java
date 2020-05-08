package org.sjtugo.api.entity;

import lombok.Data;

@Data
public class CarRoute extends Route{
    private String departID;

    private String arriveID;

    private int distance;

    private int cost;
}
