package org.sjtugo.api.service.NavigateService;

import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.entity.Route;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.WalkRoute;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class WalkPlanner extends AbstractPlanner {

    public WalkPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                       DestinationRepository destinationRepository,
                       RestTemplate restTemplate){
        super(mapVertexInfoRepository,destinationRepository,restTemplate,
                null,null,null);
    }


    @Override
    public Strategy planOne(String beginPlace, String endPlace, LocalDateTime departTime){
        NavigatePlace start = parsePlace(beginPlace);
        NavigatePlace end = parsePlace(endPlace);
        WalkRoute walkRoute = planWalkTencent(start,end);
        Strategy result = new Strategy();
        result.setType("步行");
        result.setArrive(end.getPlaceName());
        result.setCost(0);
        result.setDepart(start.getPlaceName());
        result.setDistance(walkRoute.getDistance());
        result.setPreference(new ArrayList<>()); //TODO
        result.setPass(new ArrayList<>());
        result.setTravelTime(Duration.ofSeconds(walkRoute.getRouteTime()));
        ArrayList<Route> routePlans = new ArrayList<>();
        routePlans.add(walkRoute);
        result.setRouteplan(routePlans);
        return result;
    }
}
