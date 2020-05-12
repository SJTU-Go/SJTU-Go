package org.sjtugo.api.service.planner;

import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.entity.Route;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.WalkRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class WalkPlanner extends AbstractPlanner {

    public WalkPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                       DestinationRepository destinationRepository,
                       RestTemplate restTemplate){
        super(mapVertexInfoRepository,destinationRepository,restTemplate);
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace){
        navigatePlace start = parsePlace(beginPlace);
        navigatePlace end = parsePlace(endPlace);
        Map<String,String> params=new HashMap<>();
        params.put("from", start.getLocation().getCoordinate().y
                +","+ start.getLocation().getCoordinate().x);
        params.put("key","I6IBZ-BCZRI-FHYGG-523D4-3W3C7-X6BRS");
        params.put("to", end.getLocation().getCoordinate().y
                +","+ end.getLocation().getCoordinate().x);
//        System.out.println(params);
        ResponseEntity<WalkResponse> tencentResponse =
                restTemplate.getForEntity("https://apis.map.qq.com/ws/direction/v1/walking/?from={from}&" +
                                "to={to}&key={key}", WalkResponse.class,params);
//        System.out.println(tencentResponse.getBody());
        WalkRoute walkRoute = new WalkRoute();
        walkRoute.setArriveLocation(end.getLocation());
        walkRoute.setArriveName(end.getPlaceName());
        walkRoute.setDepartName(start.getPlaceName());
        walkRoute.setDepartLocation(start.getLocation());
        walkRoute.setDistance(Objects.requireNonNull(tencentResponse.getBody()).getDistance());
        walkRoute.setRouteTime((int) tencentResponse.getBody().getTime().toSeconds());
        walkRoute.setRoutePath(tencentResponse.getBody().getRoute());

        Strategy result = new Strategy();
        result.setType("步行");
        result.setArrive(end.getPlaceName());
        result.setCost(0);
        result.setDepart(start.getPlaceName());
        result.setDistance(walkRoute.getDistance());
        result.setPreference(new ArrayList<>()); //TODO
        result.setPass(new ArrayList<>());
        result.setTravelTime(tencentResponse.getBody().getTime());
        ArrayList<Route> routePlans = new ArrayList<>();
        routePlans.add(walkRoute);
        result.setRouteplan(routePlans);
        return result;
    }
}
