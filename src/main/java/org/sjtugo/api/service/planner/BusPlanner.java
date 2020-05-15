package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.LineString;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.WalkRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class BusPlanner extends AbstractPlanner {

    public BusPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                      DestinationRepository destinationRepository,
                      RestTemplate restTemplate,
                      BusTimeVacationRepository busTimeVacationRepository,
                      BusTimeWeekdayRepository busTimeWeekdayRepository,
                      BusStopRepository busStopRepository){
        super(mapVertexInfoRepository,destinationRepository,restTemplate,
                busTimeVacationRepository,busTimeWeekdayRepository,busStopRepository);
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace){
        navigatePlace start = parsePlace(beginPlace);
        navigatePlace end = parsePlace(endPlace);
        List<BusStop> busStops = busStopRepository.getAllCounterBus();
//        System.out.println(busStops);
        System.out.println(findNearest(busStops,start));
        return null;
    }

    private BusStop findNearest(List<BusStop> busStops, navigatePlace current){
        Map<String,String> params=new HashMap<>();
        List<String> fromPlaces = busStops.stream()
                .map(stop -> stop.getLocationPlatform().getCoordinate().y +
                        "," + stop.getLocationPlatform().getCoordinate().x)
                .collect(Collectors.toList());
        params.put("to", String.join(";",fromPlaces));
        params.put("key","BHBBZ-RTA3U-ICAVZ-23DAQ-C4BQ3-V7FCX");
        params.put("from", current.getLocation().getCoordinate().y
                +","+ current.getLocation().getCoordinate().x);
////        System.out.println(params);
        ResponseEntity<NearBusResponse> tencentResponse =
                restTemplate.getForEntity("http://apis.map.qq.com/ws/distance/v1/matrix?from={from}&" +
                        "to={to}&key={key}&mode=walking", NearBusResponse.class,params);
//        System.out.print(tencentResponse.getStatusCode());
//        System.out.println(tencentResponse.getHeaders());
        System.out.println("-----Finding BusStop------");
        System.out.println(params);
        System.out.println(tencentResponse);
        return busStops.get(Objects.requireNonNull(tencentResponse.getBody())
                            .getNearestBus() - 1);
    }
}
