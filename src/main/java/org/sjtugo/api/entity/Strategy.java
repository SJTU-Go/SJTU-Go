package org.sjtugo.api.entity;


import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.time.Duration;
import java.util.List;

@Data
public class Strategy {
    private String type;

    private String depart;

    private String arrive;

    private List<String> pass;

    private Duration travelTime;

    private int distance;

    private int cost;

    private List<String> preference;

    private List<Route> routeplan;

    public void merge(Strategy nextStrategy){
        // pre-condition: self.arrive == nextStrategy.depart, nextStrategy.pass = {}
        pass.add(arrive);
        arrive = nextStrategy.getArrive();
        travelTime = travelTime.plus(nextStrategy.travelTime);
        distance += nextStrategy.distance;
        cost += nextStrategy.cost;
        routeplan.addAll(nextStrategy.routeplan);
    }
}
