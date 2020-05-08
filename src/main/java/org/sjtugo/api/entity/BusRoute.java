package org.sjtugo.api.entity;

import lombok.Data;
import java.time.*;

@Data
public class BusRoute extends Route {
    private String departID;

    private String arriveID;

    private LocalTime departTime;

    private LocalTime arriveTime;

    private int distance;
}
