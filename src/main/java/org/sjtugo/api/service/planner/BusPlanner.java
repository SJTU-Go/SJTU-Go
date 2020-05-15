package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import org.apache.tomcat.jni.Local;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.entity.BusRoute;
import org.sjtugo.api.entity.Route;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.WalkRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        NavigatePlace start = parsePlace(beginPlace);
        NavigatePlace end = parsePlace(endPlace);
        List<BusStop> busStops = busStopRepository.getAllCounterStartBus(); // 2~19
        BusStop startBus = findNearest(busStops,start);
        busStops = busStopRepository.getAllCounterArriveBus(); // 1~18
        BusStop endBus = findNearest(busStops,end);
        if (startBus.getStopId() < endBus.getStopId()) { // 顺时针
            startBus = busStopRepository.findById( - startBus.getStopId()).orElse(null);
            endBus = busStopRepository.findById( - endBus.getStopId()).orElse(null);
        }
        WalkRoute toBus = planWalkTencent(start,new NavigatePlace(startBus));
        BusRoute busRoute = planBus(startBus,endBus,
                LocalTime.now().plus(Duration.ofSeconds(toBus.getRouteTime())));
        WalkRoute fromBus = planWalkTencent(new NavigatePlace(endBus),end);

        Strategy result = new Strategy();
        result.setType("校园巴士");
        result.setArrive(end.getPlaceName());
        result.setCost(0);
        result.setDepart(start.getPlaceName());
        result.setDistance(toBus.getDistance()+fromBus.getDistance());
        result.setPreference(new ArrayList<>()); //TODO
        result.setPass(new ArrayList<>());
        result.setTravelTime(Duration.ofSeconds(toBus.getRouteTime()
                                + busRoute.getRouteTime()
                                + fromBus.getRouteTime()));
        ArrayList<Route> routePlans = new ArrayList<>();
        routePlans.add(toBus);
        routePlans.add(busRoute);
        routePlans.add(fromBus);
        result.setRouteplan(routePlans);
        return result;
    }

    private BusRoute planBus(BusStop startBus, BusStop endBus, LocalTime departTime) {
        BusRoute busRoute = new BusRoute();
        if (isWeekday()) {
            List<LocalTime> getOnTimes;
            LocalTime getOnTime;
            if (isVacation()){
                getOnTimes = StreamSupport
                        .stream(busTimeVacationRepository.findAll().spliterator(),false)
                        .map(timeElem -> timeElem.getBusTime().plus(startBus.getDiff()))
                        .filter(timeElem -> timeElem.isAfter(departTime))
                        .collect(Collectors.toList());
                getOnTime = getOnTimes.get(0);
                if (getOnTime == null) { return null; }
            } else {
                getOnTimes = StreamSupport
                        .stream(busTimeWeekdayRepository.findAll().spliterator(),false)
                        .map(timeElem -> timeElem.getBusTime().plus(startBus.getDiff()))
                        .filter(timeElem -> timeElem.isAfter(departTime)) // TODO DATETIME
                        .collect(Collectors.toList());
                getOnTime = getOnTimes.get(0);
                if (getOnTime == null) { return null; }
            }
            LocalTime getOffTime = getOnTime.plus(endBus.getDiff()).minus(startBus.getDiff());
            busRoute.setArriveID("BUS" + endBus.getStopId());
            busRoute.setArriveTime(getOffTime);
            busRoute.setDepartID("BUS" + startBus.getStopId());
            busRoute.setDepartTime(getOnTime);
            busRoute.setDepartName(startBus.getStopName()); // TODO 标方向
            busRoute.setArriveName(endBus.getStopName());
            busRoute.setRouteTime((int) Duration.between(getOnTime,getOffTime).toSeconds());
            List<LineString> routePaths = busStopRepository.
                            findByStopIdBetween(endBus.getStopId(),startBus.getStopId()-1)
                            .stream()
                            .map(BusStop::getNextRoute)
                            .collect(Collectors.toList());
            LineMerger merger = new LineMerger();
            for (LineString nextRoute : routePaths) {
                merger.add(nextRoute);
            }
            Collection<LineString> collection = merger.getMergedLineStrings();
            busRoute.setRoutePath(collection.iterator().next());
            return busRoute;
        } else {
            return null;
        }
    }

    private BusStop findNearest(List<BusStop> busStops, NavigatePlace current){
        Map<String,String> params=new HashMap<>();
        List<String> fromPlaces = busStops.stream()
                .map(stop -> stop.getLocationPlatform().getCoordinate().y +
                        "," + stop.getLocationPlatform().getCoordinate().x)
                .collect(Collectors.toList());
        params.put("to", String.join(";",fromPlaces));
        params.put("key","BHBBZ-RTA3U-ICAVZ-23DAQ-C4BQ3-V7FCX");
        params.put("from", current.getLocation().getCoordinate().y
                +","+ current.getLocation().getCoordinate().x);
        ResponseEntity<NearBusResponse> tencentResponse =
                restTemplate.getForEntity("http://apis.map.qq.com/ws/distance/v1/matrix?from={from}&" +
                        "to={to}&key={key}&mode=walking", NearBusResponse.class,params);

        System.out.println("-----Finding BusStop------");
        System.out.println(params);
        System.out.println(tencentResponse);
        return busStops.get(Objects.requireNonNull(tencentResponse.getBody())
                            .getNearestBus() - 1);
    }

    private Boolean isWeekday() {
        Calendar cal = Calendar.getInstance();
        return ! (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }

    private Boolean isVacation() {
        Calendar summberBegin = Calendar.getInstance();
        summberBegin.clear();
        summberBegin.set(2020,7,26);
        Calendar summerEnd = Calendar.getInstance();
        summerEnd.clear();
        summerEnd.set(2020,9,6);
        Calendar winterBegin = Calendar.getInstance();
        winterBegin.clear();
        winterBegin.set(2021,1,11);
        Calendar winterEnd = Calendar.getInstance();
        winterEnd.clear();
        winterEnd.set(2020,2,22);
        Calendar cal = Calendar.getInstance();
        return (cal.before(summerEnd) && cal.after(summberBegin))
                || (cal.before(winterEnd) && cal.after(winterBegin));
    }
}
