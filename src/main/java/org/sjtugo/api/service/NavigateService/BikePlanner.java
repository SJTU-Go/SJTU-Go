package org.sjtugo.api.service.NavigateService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.Entity.VertexDestination;
import org.sjtugo.api.DAO.Entity.VertexDestinationID;
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

public class BikePlanner extends AbstractPlanner {

    public BikePlanner(MapVertexInfoRepository mapVertexInfoRepository,
                       DestinationRepository destinationRepository,
                       RestTemplate restTemplate,
                       BusTimeRepository busTimeRepository,
                       BusStopRepository busStopRepository,
                       VertexDestinationRepository vertexDestinationRepository){
        super(mapVertexInfoRepository,destinationRepository,restTemplate,
                busTimeRepository,busStopRepository,vertexDestinationRepository,
                null);
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace, LocalDateTime departTime, Boolean avoidTraffic){
        // 匹配地点
        NavigatePlace start = parsePlace(beginPlace);
        NavigatePlace end = parsePlace(endPlace);
        List<Route> routeList = new ArrayList<>();
        List<MapVertexInfo> pickups;
        MapVertexInfo fstPickup;
//        List<MapVertexInfo> parkbikes;
        MapVertexInfo fstParkbike;

        // PICK UP
        if (start.getPlaceType() != NavigatePlace.PlaceType.parking) {
//
            if (start.getPlaceType() == NavigatePlace.PlaceType.destination) {
                TransitionRoute startTransition = new TransitionRoute(true);
                VertexDestination transInfo = vertexDestinationRepository
                        .findByPlaceid(start.getPlaceID());
                startTransition.setRouteTime(transInfo.getReachtime());
                fstPickup = mapVertexInfoRepository
                        .findById(transInfo.getVertexid()).orElseThrow();
                routeList.add(startTransition);
            } else {
                pickups = nearsetParking(start);
                TransitionRoute startTransition = new TransitionRoute(true, start, pickups);
                routeList.add(startTransition);
                fstPickup = pickups.get(0);
            }
        }
        else {
            fstPickup = mapVertexInfoRepository.findById(start.getPlaceID()).orElseThrow();
        }


        // Shortest Path
        if (end.getPlaceType() != NavigatePlace.PlaceType.parking) {
            if (end.getPlaceType() == NavigatePlace.PlaceType.destination){
                VertexDestination transInfo = vertexDestinationRepository
                        .findByPlaceid(end.getPlaceID());
                fstParkbike = mapVertexInfoRepository.findById(transInfo.getVertexid()).orElseThrow();
            } else {
                fstParkbike = nearsetParking(end).get(0);
            }
        } else {
            fstParkbike = mapVertexInfoRepository.findById(end.getPlaceID()).orElseThrow();
        }
        try{
            routeList.add(planBike(fstPickup,fstParkbike,avoidTraffic));
        } catch (ParseException e){
            throw new StrategyNotFoundException("bike route not found");
        }





        // Parking
        if (end.getPlaceType() != NavigatePlace.PlaceType.parking) {
            TransitionRoute endTransition = new TransitionRoute(false);
            if (end.getPlaceType() == NavigatePlace.PlaceType.destination) {
                VertexDestination transInfo = vertexDestinationRepository
                        .findById(new VertexDestinationID(end.getPlaceID(),fstParkbike.getVertexID())).orElseThrow();
                endTransition.setRouteTime(transInfo.getReachtime());
            } else {
                endTransition.setRouteTime(0);
            }
            endTransition.setParkID(String.valueOf(fstParkbike.getVertexID()));
            endTransition.setPlaceID(end.getPlaceType().toString() + end.getPlaceID());
            endTransition.setArriveName(end.getPlaceName());
            endTransition.setDepartName(fstParkbike.getVertexName() + "（停车点）");
            endTransition.setRoutePath(new GeometryFactory().createLineString(
                    new Coordinate[]{fstParkbike.getLocation().getCoordinate(), end.getLocation().getCoordinate()}
            ));
            routeList.add(endTransition);
        }
        Strategy result = new Strategy();
        result.setType("共享单车");
        result.setArrive(end.getPlaceName());
        result.setCost(100);
        result.setDepart(start.getPlaceName());
        result.setDistance(routeList.stream().mapToInt(Route::getDistance).sum());
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

    @SuppressWarnings("unchecked")
    private BikeRoute planBike(MapVertexInfo begin, MapVertexInfo end, Boolean avoidTraffic) throws ParseException {
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
                                      "total_time: SUM(p.edges[*].normalBikeTime)," +
                                      "total_distance: SUM(p.edges[*].distance)  }");
        Map<String,String> bindVars = new HashMap<>();
        bindVars.put("f","vertex/"+ beginID);
        bindVars.put("to","vertex/"+endID);
        bindVars.put("edge","bikeedge");
        if (avoidTraffic){
            bindVars.put("attribute","avoidBikeTime");
        } else{
            bindVars.put("attribute","normalBikeTime");
        }
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
//        System.out.println(arrangoResult.get("total_distance"));
//        System.out.println(arrangoResult.get("total_distance").getClass());
        result.setRideDistance(((Double) arrangoResult.get("total_distance")).intValue());
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
