package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import io.swagger.models.auth.In;
import org.json.simple.JSONObject;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.entity.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class BikePlanner extends AbstractPlanner {

    public BikePlanner(MapVertexInfoRepository mapVertexInfoRepository,
                       DestinationRepository destinationRepository,
                       RestTemplate restTemplate,
                       BusTimeRepository busTimeRepository,
                       BusStopRepository busStopRepository,
                       VertexDestinationRepository vertexDestinationRepository){
        super(mapVertexInfoRepository,destinationRepository,restTemplate,
                busTimeRepository,busStopRepository,vertexDestinationRepository);
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace, LocalDateTime departTime){
        // 匹配公交站
        NavigatePlace start = parsePlace(beginPlace);
        NavigatePlace end = parsePlace(endPlace);
        List<Route> routeList = new ArrayList<>();
        MapVertexInfo pickup;
        MapVertexInfo parkbike;

        // PICK UP
        if (start.getPlaceType() != NavigatePlace.PlaceType.parking) {
            TransitionRoute startTransition = new TransitionRoute(true);
            if (start.getPlaceType() == NavigatePlace.PlaceType.destination) {
                VertexDestination transInfo = vertexDestinationRepository
                        .findByPlaceid(start.getPlaceID());
                startTransition.setRouteTime(transInfo.getReachtime());
                pickup = mapVertexInfoRepository.findById(transInfo.getVertexid()).orElseThrow();
            } else {
                startTransition.setRouteTime(0);
                pickup = nearsetParking(start);
            }
            startTransition.setParkID(String.valueOf(pickup.getVertexID()));
            startTransition.setPlaceID(start.getPlaceType().toString() + start.getPlaceID());
            startTransition.setDepartName(start.getPlaceName());
            startTransition.setArriveName(pickup.getVertexName() + "（寻车点）");
            startTransition.setRoutePath(new GeometryFactory().createLineString(
                    new Coordinate[]{start.getLocation().getCoordinate(), pickup.getLocation().getCoordinate()}
            ));
            routeList.add(startTransition);
        } else {
            pickup = mapVertexInfoRepository.findById(start.getPlaceID()).orElseThrow();
        }


        // Shortest Path
        if (end.getPlaceType() != NavigatePlace.PlaceType.parking) {
            if (end.getPlaceType() == NavigatePlace.PlaceType.destination){
                VertexDestination transInfo = vertexDestinationRepository
                        .findByPlaceid(end.getPlaceID());
                parkbike = mapVertexInfoRepository.findById(transInfo.getVertexid()).orElseThrow();
            } else {
                parkbike = nearsetParking(end);
            }
        } else {
            parkbike = mapVertexInfoRepository.findById(end.getPlaceID()).orElseThrow();
        }
        try{
            routeList.add(planBike(pickup,parkbike));
        } catch (ParseException e){
            throw new StrategyNotFoundException("bike route not found");
        }





        // Parking
        if (end.getPlaceType() != NavigatePlace.PlaceType.parking) {
            TransitionRoute endTransition = new TransitionRoute(false);
            if (end.getPlaceType() == NavigatePlace.PlaceType.destination) {
                VertexDestination transInfo = vertexDestinationRepository
                        .findById(new VertexDestinationID(end.getPlaceID(),parkbike.getVertexID())).orElseThrow();
                endTransition.setRouteTime(transInfo.getReachtime());
            } else {
                endTransition.setRouteTime(0);
            }
            endTransition.setParkID(String.valueOf(parkbike.getVertexID()));
            endTransition.setPlaceID(end.getPlaceType().toString() + end.getPlaceID());
            endTransition.setArriveName(end.getPlaceName());
            endTransition.setDepartName(parkbike.getVertexName() + "（停车点）");
            endTransition.setRoutePath(new GeometryFactory().createLineString(
                    new Coordinate[]{parkbike.getLocation().getCoordinate(), end.getLocation().getCoordinate()}
            ));

            routeList.add(endTransition);
        }
        Strategy result = new Strategy();
        result.setType("共享单车");
        result.setArrive(end.getPlaceName());
        result.setCost(150);
        result.setDepart(start.getPlaceName());
        result.setDistance(0);
        result.setPreference(new ArrayList<>()); //TODO
        result.setPass(new ArrayList<>());
        result.setTravelTime(Duration.ofSeconds(routeList
                .stream().mapToInt(Route::getRouteTime).sum()));
        result.setRouteplan(routeList);
        return result;
    }

    @SuppressWarnings("unchecked")
    private BikeRoute planBike(MapVertexInfo begin, MapVertexInfo end) throws ParseException {
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
                                      "total_time: SUM(p.edges[*].@attribute)," +
                                      "total_distance: SUM(p.edges[*].distance)  }");
        Map<String,String> bindVars = new HashMap<>();
        bindVars.put("f","vertex/"+ beginID);
        bindVars.put("to","vertex/"+endID);
        bindVars.put("edge","bikeedge");
        bindVars.put("attribute","normalBikeTime"); //TODO TRAFFIC
        params.put("bindVars",bindVars);
        params.put("count",true);
        params.put("batchSize",1);


        HttpEntity<Object> request = new HttpEntity<>(params,headers);

        LinkedHashMap<String,Object> arrangoResponse =
                restTemplate.postForObject("http://47.92.147.237:8529/_api/cursor",request,LinkedHashMap.class);
        assert arrangoResponse != null;
        LinkedHashMap<String,Object> arrangoResult =
                ((List<LinkedHashMap<String,Object>>) arrangoResponse.get("result")).get(0);


        BikeRoute result = new BikeRoute();
        result.setDepartID("PK"+begin.getVertexID());
        result.setArriveID("PK"+end.getVertexID());
        result.setCost(150);
        System.out.println(arrangoResult.get("total_distance"));
        System.out.println(arrangoResult.get("total_distance").getClass());
        result.setDistance(((Double) arrangoResult.get("total_distance")).intValue());
        result.setRouteTime(((Double) arrangoResult.get("total_time")).intValue());
        result.setMethod("哈罗单车");
//        System.out.println(arrangoResult.get("vertex").getClass());
        result.setPassingVertex(((ArrayList<String>) arrangoResult.get("vertex")));
        result.setDepartName(begin.getVertexName()+"（寻车点）");
        result.setArriveName(end.getVertexName()+"（停车点）");
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
