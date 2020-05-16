package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.entity.BusRoute;
import org.sjtugo.api.entity.Route;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.WalkRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BusPlanner extends AbstractPlanner {

    public BusPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                      DestinationRepository destinationRepository,
                      RestTemplate restTemplate,
                      BusTimeRepository busTimeRepository,
                      BusStopRepository busStopRepository){
        super(mapVertexInfoRepository,destinationRepository,restTemplate,
                busTimeRepository,busStopRepository);
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace, LocalDateTime departTime){
        // 匹配公交站
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


        // 计算导航
        assert startBus != null;
        assert endBus != null;

        WalkRoute toBus = planWalkTencent(start,new NavigatePlace(startBus));
        BusRoute busRoute = planBus(startBus,endBus,
                departTime.plus(Duration.ofSeconds(toBus.getRouteTime())));
        WalkRoute fromBus = planWalkTencent(new NavigatePlace(endBus),end);
        if (busRoute == null) {return null;}
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

    private BusRoute planBus(BusStop startBus, BusStop endBus, LocalDateTime departTime) {
        BusRoute busRoute = new BusRoute();
//        System.out.println((busTimeRepository.findAllByBusTypeEquals(
//                ((startBus.getStopId() > 0) ? 1 : -1  *
//                        (isWeekday(departTime) ? ( (isVacation(departTime) ? 2 : 1) ) : 0)))));
//        System.out.println( (busTimeRepository.findAllByBusTypeEquals(
//                ((startBus.getStopId() > 0) ? 1 : -1  *
//                        (isWeekday(departTime) ? ( (isVacation(departTime) ? 2 : 1) ) : 0))))
//                .stream()
//                .map(timeElem -> timeElem.getBusTime().plus(startBus.getDiff())));
        List<LocalTime> getOnTimes = (busTimeRepository.findAllByBusTypeEquals(
                ((startBus.getStopId() > 0) ? 1 : -1  *
                        (isWeekday(departTime) ? ( (isVacation(departTime) ? 2 : 1) ) : 0))))
                .stream()
                .map(timeElem -> timeElem.getBusTime().plus(startBus.getDiff()))
                .filter(timeElem -> timeElem.isAfter(LocalTime.from(departTime)))
                .collect(Collectors.toList());
        if (!getOnTimes.isEmpty())
        {
            LocalTime getOnTime = getOnTimes.get(0);
//            System.out.println(getOnTime);
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

//        System.out.println("-----Finding BusStop------");
//        System.out.println(params);
//        System.out.println(tencentResponse);
        int busStopNum = Objects.requireNonNull(tencentResponse.getBody())
                .getNearestBus() - 1;
        return busStops.get(busStopNum < 0 ? busStops.size()-1 : busStopNum);
    }

    private Boolean isWeekday(LocalDateTime now) {
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return ! (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }

    private Boolean isVacation(LocalDateTime now) {
        Calendar summberBegin = Calendar.getInstance();
        summberBegin.clear();
        summberBegin.set(2020,Calendar.JULY,26);
//        System.out.println(summberBegin);
        Calendar summerEnd = Calendar.getInstance();
        summerEnd.clear();
        summerEnd.set(2020, Calendar.SEPTEMBER,6);
        Calendar winterBegin = Calendar.getInstance();
        winterBegin.clear();
        winterBegin.set(2021, Calendar.JANUARY,11);
        Calendar winterEnd = Calendar.getInstance();
        winterEnd.clear();
        winterEnd.set(2020, Calendar.FEBRUARY,22);
        Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
//        System.out.println(cal);
//        System.out.println(cal.before(summerEnd));
//        System.out.println(cal.after(summberBegin));
        return (cal.before(summerEnd) && cal.after(summberBegin))
                || (cal.before(winterEnd) && cal.after(winterBegin));
    }
}
