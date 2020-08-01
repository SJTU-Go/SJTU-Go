package org.sjtugo.api.service.NavigateService;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.DAO.Entity.*;
import org.sjtugo.api.entity.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class JindouyunPlanner extends AbstractPlanner {

    private final MotorForbidAreaRepository motorForbidAreaRepository;
    private final JindouyunRepository jindouyunRepository;

    public JindouyunPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                       DestinationRepository destinationRepository,
                       RestTemplate restTemplate,
                       BusTimeRepository busTimeRepository,
                       BusStopRepository busStopRepository,
                       VertexDestinationRepository vertexDestinationRepository,
                            MotorForbidAreaRepository motorForbidAreaRepository,
                            JindouyunRepository jindouyunRepository){
        super(mapVertexInfoRepository,destinationRepository,restTemplate,
                busTimeRepository,busStopRepository,vertexDestinationRepository,null);
        this.motorForbidAreaRepository = motorForbidAreaRepository;
        this.jindouyunRepository = jindouyunRepository;
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace, LocalDateTime departTime, Boolean avoidTraffic){
        NavigatePlace start = parsePlace(beginPlace);
        NavigatePlace end = parsePlace(endPlace);

        JindouyunInfo objectCar = jindouyunRepository
                .findNearbyBikes(start.getLocation().getCoordinate().x, start.getLocation().getCoordinate().y).get(0);
        MapVertexInfo parkCar = mapVertexInfoRepository.findNearbyCarPoint(nearbyWindow(end.getLocation())).get(0);

        List<Route> routeList = new ArrayList<>();

        // 步行取车
        WalkRoute findCar = planWalkTencent(start,new NavigatePlace(objectCar));
        routeList.add(findCar);
        Strategy result = new Strategy();
        result.setDistance(findCar.getDistance());
        // 驾车
        try {
            CarRoute driveCar = planCar(
                    mapVertexInfoRepository.getOne(Integer.parseInt(objectCar.getClusterPoint())),
                    parkCar,avoidTraffic);
            routeList.add(driveCar);
            result.setCost(driveCar.getCost());
        } catch (ParseException e) {
            throw new StrategyNotFoundException("Current Car unavailable");
        }
        result.setType("筋斗云");
        result.setArrive(end.getPlaceName());
        result.setDepart(start.getPlaceName());
        if (avoidTraffic){
            result.setPreference(List.of("避开拥堵"));
        } else{
            result.setPreference(new ArrayList<>());
        }
        result.setPass(new ArrayList<>());
        result.setTravelTime(Duration.ofSeconds(routeList
                .stream().mapToInt(Route::getRouteTime).sum()));
        result.setRouteplan(routeList);
        result.setPassDetail(new ArrayList<>());
        result.setBeginDetail(start);
        result.setEndDetail(end);
        return result;
    }

    private Polygon nearbyWindow(Point t){
        double lng_shift = 0.01;
        double lat_shift = 0.01;
        double x = t.getCoordinate().x;
        double y = t.getCoordinate().y;
        return new GeometryFactory().createPolygon(
                new Coordinate[]{
                        new Coordinate(x - lng_shift, y - lat_shift),
                        new Coordinate(x-lng_shift,y+lat_shift),
                        new Coordinate(x+lng_shift,y+lat_shift),
                        new Coordinate(x+lng_shift,y-lat_shift),
                        new Coordinate(x - lng_shift, y - lat_shift)
                });
    }

    @SuppressWarnings("unchecked")
    private CarRoute planCar(MapVertexInfo begin, MapVertexInfo end, Boolean avoidTraffic) throws ParseException {
        Integer beginID = begin.getVertexID();
        Integer endID = end.getVertexID();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,Object> params = new HashMap<>();
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor("public", ""));
        params.put("query","FOR p IN OUTBOUND K_SHORTEST_PATHs " +
                "@f TO @to  @edge OPTIONS  {weightAttribute: @attribute}  LIMIT 1 " +
                "RETURN {  vertex: p.vertices[*]._key, " +
                "locations: p.vertices[*].location ," +
                "total_time: SUM(p.edges[*].normalMotorTime)," +
                "total_distance: SUM(p.edges[*].distance)  }");
        Map<String,String> bindVars = new HashMap<>();
        bindVars.put("f","vertex/"+ beginID);
        bindVars.put("to","vertex/"+endID);
        bindVars.put("edge","bikeedge");
        if (avoidTraffic){
            bindVars.put("attribute","avoidMotorTime");
        } else{
            bindVars.put("attribute","normalMotorTime");
        }
        params.put("bindVars",bindVars);
//        System.out.println(bindVars);
        params.put("count",true);
        params.put("batchSize",1);


        HttpEntity<Object> request = new HttpEntity<>(params,headers);

        LinkedHashMap<String,Object> arrangoResponse =
                restTemplate.postForObject("http://47.92.147.237:8529/_api/cursor",request,LinkedHashMap.class);
        assert arrangoResponse != null;
        System.out.println(arrangoResponse);
        LinkedHashMap<String,Object> arrangoResult =
                ((List<LinkedHashMap<String,Object>>) arrangoResponse.get("result")).get(0);


        CarRoute result = new CarRoute();
        result.setDepartID("PK"+begin.getVertexID());
        result.setArriveID("PK"+end.getVertexID());
//        System.out.println(arrangoResult.get("total_distance"));
//        System.out.println(arrangoResult.get("total_distance").getClass());
        result.setDriveDistance(((Double) arrangoResult.get("total_distance")).intValue());
        result.setRouteTime(((Double) arrangoResult.get("total_time")).intValue());
        //int forbid = motorForbidAreaRepository.isFobidArea(end.getLocation().getCoordinate().x, end.getLocation().getCoordinate().y);
        int forbid = 0;
        result.setCost(100+200*(result.getRouteTime()/900 + 1) + forbid*50);
//        System.out.println(arrangoResult.get("vertex").getClass());
        result.setPassingVertex(((ArrayList<String>) arrangoResult.get("vertex")));
        result.setDepartName("寻车点");
        result.setArriveName("停车点");
//        System.out.println(arrangoResult);
//        System.out.println(arrangoResult.get("locations"));
//        System.out.println("LINESTRING (" +
//                ((ArrayList<ArrayList<Double>>) arrangoResult.get("locations")).stream()
//                        .map(elem -> elem.get(0)+ " " + elem.get(1)).collect(Collectors.joining(","))
//                +")");
        result.setRoutePath((LineString) new WKTReader().read( "LINESTRING (" +
                ((ArrayList<ArrayList<Double>>) arrangoResult.get("locations")).stream()
                        .map(elem -> elem.get(0)+ " " + elem.get(1)).collect(Collectors.joining(","))
                +")"));
        return result;
    }
}
