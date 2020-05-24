package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.controller.NavigateControl;
import org.sjtugo.api.controller.NavigateRequest;
import org.sjtugo.api.entity.Strategy;
import org.sjtugo.api.entity.WalkRoute;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 路线规划的抽象类，用于提供统一的接口，以及各类规划方案都会用到的私有函数方法
 * @author Tony Zhou
 * @version 2020.0511
 */
public abstract class AbstractPlanner {

    protected final MapVertexInfoRepository mapVertexInfoRepository;
    protected final DestinationRepository destinationRepository;
    protected final RestTemplate restTemplate;
    protected final BusTimeRepository busTimeRepository;
    protected final BusStopRepository busStopRepository;
    protected final TokenPool tokenPool;
    protected final VertexDestinationRepository vertexDestinationRepository;
    /**
     * @param mapVertexInfoRepository 注入地图信息数据库接口
     */
    public AbstractPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                           DestinationRepository destinationRepository,
                           RestTemplate restTemplate,
                           BusTimeRepository busTimeRepository,
                           BusStopRepository busStopRepository,
                           VertexDestinationRepository vertexDestinationRepository){
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.destinationRepository = destinationRepository;
        this.restTemplate = restTemplate;
        this.busTimeRepository = busTimeRepository;
        this.busStopRepository = busStopRepository;
        this.vertexDestinationRepository = vertexDestinationRepository;
        this.tokenPool = new TokenPool();
    }

    /**
     * @param beginPlace 出发地点的名称/经纬度/地点ID
     * @param endPlace 到达地点的名称/经纬度/地点ID
     * @param departTime 预设出发时间
     * @return planner对应的方案
     */
    public abstract Strategy planOne(String beginPlace, String endPlace, LocalDateTime departTime);


    /**
     * Planner类的对外接口，函数内部调用planOne方法获取单出发点、到达点的方案，将途经点拼接起来
     * @param navigateRequest 请求体
     * @return planner对应的方案
     */
    public Strategy planAll(NavigateRequest navigateRequest){
        String currentPlace = navigateRequest.getBeginPlace();
        String[] passPlaces =new String[navigateRequest.getPassPlaces().size()];
        passPlaces = navigateRequest.getPassPlaces().toArray(passPlaces);
        LocalDateTime departTime = navigateRequest.getDepartTime();
        String endPlace = navigateRequest.getArrivePlace();
        String nextPlace = passPlaces.length>0 ? passPlaces[0] : endPlace;
        int i;
        Strategy resultStrategy = planOne(currentPlace,nextPlace,departTime);
        for (i=0; i<passPlaces.length; i++) {
            currentPlace = nextPlace;
            nextPlace = i+1<passPlaces.length ? passPlaces[i+1] : endPlace;
//            System.out.print(currentPlace);
//            System.out.println(nextPlace);
            Strategy currentStrategy = planOne(currentPlace,nextPlace,
                                               departTime.plus(resultStrategy.getTravelTime()));
            resultStrategy.merge(currentStrategy);
        }
        return resultStrategy;
    }

    /**
     * 根据用户的输入到数据库中查找地点名称、位置、判断地点类型，补全导航计算所需信息
     * @param place 一个地点的名称/经纬度/地点ID，即API接受的输入值
     * @return 一个navigatePlace实例，包含名称、经纬度、地点类型，方便内部计算时调用
     */
    public NavigatePlace parsePlace(String place){
        NavigatePlace result = new NavigatePlace();
        Pattern PKpattern = Pattern.compile("(PK)([0-9]*)"); // regexp匹配数字ID
        Pattern DTpattern = Pattern.compile("(DT)([0-9]*)"); // regexp匹配数字ID
        Matcher PKmatcher = PKpattern.matcher(place);
        Matcher DTmatcher = DTpattern.matcher(place);
        if (PKmatcher.find()){
            place = PKmatcher.group(2);
            Optional<MapVertexInfo> vtx_record = mapVertexInfoRepository
                    .findById(Integer.parseInt(place));
            if (vtx_record.isPresent()) { // input is a parkingPlace
                result.setLocation(vtx_record.get().getLocation());
                result.setPlaceName(vtx_record.get().getVertexName());
                result.setPlaceType(NavigatePlace.PlaceType.parking);
                result.setPlaceID(Integer.parseInt(place));
                return result;
            }
        }
        if (DTmatcher.find()){
            place = DTmatcher.group(2);
            Optional<Destination> dst_record = destinationRepository
                    .findById(Integer.parseInt(place));
            if (dst_record.isPresent()){ // input is a destination
                result.setLocation(dst_record.get().getLocation());
                result.setPlaceName(dst_record.get().getPlaceName());
                result.setPlaceType(NavigatePlace.PlaceType.destination);
                result.setPlaceID(Integer.parseInt(place));
                return result;
            }
        }
        try { // 读取坐标
            Point loc = (Point) new WKTReader().read(place);
            result.setLocation(loc);
            result.setPlaceName(loc.toString()+"附近的位置");
            result.setPlaceType(NavigatePlace.PlaceType.point);
            return result;
        } catch (ParseException e) { // 调用外部地图API定位
            Map<String,String> params=new HashMap<>();
            params.put("keyword",place);
            params.put("boundary","rectangle(31.016309,121.423743,31.033088,121.449057)");
            params.put("key",tokenPool.getToken());
            params.put("page_index","1");
            params.put("page_size","10");
            ResponseEntity<PlaceResponse> tencentResponse;
            tencentResponse =
                    restTemplate.getForEntity("https://apis.map.qq.com/ws/place/v1/search?keyword={keyword}" +
                                    "&boundary={boundary}&key={key}&page_index={page_index}&page_size={page_size}",
                            PlaceResponse.class,params);
//            System.out.println("-----Finding Place------");
//            System.out.println(params);
//            System.out.println(tencentResponse);
            result.setLocation(Objects.requireNonNull(Objects.requireNonNull(tencentResponse.getBody(),
                    "Search Place Result Not Found").getLocation(),
                    "Place Location Not Found"));
            result.setPlaceName(Objects.requireNonNull(tencentResponse.getBody(),
                    "Place Name Not Found").getTitle());
            result.setPlaceType(NavigatePlace.PlaceType.point);

//            System.out.println(result);
            return result;
        }
    }


    /**
     * 调用腾讯地图，规划步行路径，用于纯步行方案和校园巴士方案
     * @param start 出发点的地名、坐标、类型
     * @param end 到达点的地名、坐标、类型
     * @return 一条WalkRoute
     */

    protected WalkRoute planWalkTencent (NavigatePlace start, NavigatePlace end){
        Map<String,String> params=new HashMap<>();
        params.put("from", start.getLocation().getCoordinate().y
                +","+ start.getLocation().getCoordinate().x);
        params.put("key",tokenPool.getToken());
        params.put("to", end.getLocation().getCoordinate().y
                +","+ end.getLocation().getCoordinate().x);
//        System.out.println(params);
        ResponseEntity<WalkResponse> tencentResponse =
                restTemplate.getForEntity("https://apis.map.qq.com/ws/direction/v1/walking/?from={from}&" +
                        "to={to}&key={key}", WalkResponse.class,params);
//        System.out.print(tencentResponse.getStatusCode());
//        System.out.println(tencentResponse.getHeaders());
//        System.out.println(tencentResponse);
        WalkRoute walkRoute = new WalkRoute();
        walkRoute.setArriveLocation(end.getLocation());
        walkRoute.setArriveName(end.getPlaceName());
        walkRoute.setDepartName(start.getPlaceName());
        walkRoute.setDepartLocation(start.getLocation());
        walkRoute.setDistance(Objects.requireNonNull(tencentResponse.getBody(),
                "Walk Route Response not fetched")
                .getDistance());
        walkRoute.setRouteTime((int) Objects.requireNonNull(tencentResponse.getBody().getTime(),"Route time not fetched")
                .toSeconds());
        walkRoute.setRoutePath(Objects.requireNonNull(tencentResponse.getBody(),"Route not fetched").getRoute());
        return walkRoute;
    }

    protected MapVertexInfo nearsetParking (NavigatePlace point){
        Coordinate pointCoord = point.getLocation().getCoordinate();
        return mapVertexInfoRepository.findNearbyParking(pointCoord.x,pointCoord.y).get(0);
    }


}

