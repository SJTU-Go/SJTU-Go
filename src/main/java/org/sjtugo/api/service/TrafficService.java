package org.sjtugo.api.service;

import org.hibernate.internal.util.collections.BoundedConcurrentHashMap;
import org.sjtugo.api.controller.ResponseEntity.TrafficInfoResponse;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class TrafficService {
    public void timeSetMenus() {
        System.out.println(123);
    }

    public void updateTraffic(TrafficInfo trafficInfo){

    }

    public void restoreTraffic(TrafficInfo trafficInfo){


    }

    public List<TrafficInfoResponse> currentTraffic(){
        return null;
    }

}
