package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import lombok.Data;
import org.sjtugo.api.DAO.Destination;
import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.DAO.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.entity.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 路线规划的抽象类，用于提供统一的接口，以及各类规划方案都会用到的私有函数方法
 * @author Tony Zhou
 * @version 2020.0511
 */
public abstract class AbstractPlanner {

    private final MapVertexInfoRepository mapVertexInfoRepository;
    private final DestinationRepository destinationRepository;
    private final RestTemplate restTemplate;

    /**
     * @param mapVertexInfoRepository 注入地图信息数据库接口
     */
    public AbstractPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                           DestinationRepository destinationRepository,
                           RestTemplate restTemplate){
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.destinationRepository = destinationRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * @param beginPlace 出发地点的名称/经纬度/地点ID
     * @param endPlace 到达地点的名称/经纬度/地点ID
     * @return planner对应的方案
     */
    public abstract Strategy planOne(String beginPlace, String endPlace);


    /**
     * Planner类的对外接口，函数内部调用planOne方法获取单出发点、到达点的方案，将途经点拼接起来
     * @param beginPlace 出发地点的名称/经纬度/地点ID
     * @param passPlaces 途径地点的名称/经纬度/地点ID的list
     * @param endPlace 到达地点的名称/经纬度/地点ID
     * @return planner对应的方案
     */
    public Strategy planAll(String beginPlace, String[] passPlaces, String endPlace){
        String currentPlace = beginPlace;
        String nextPlace = passPlaces.length>0 ? passPlaces[0] : endPlace;
        int i;
        Strategy resultStrategy = planOne(currentPlace,nextPlace);
        for (i=0; i<passPlaces.length; i++) {
            Strategy currentStrategy = planOne(currentPlace,nextPlace);
            resultStrategy.merge(currentStrategy);
            currentPlace = passPlaces[i];
            nextPlace = i+1<passPlaces.length ? passPlaces[i+1] : endPlace;
        }
        return resultStrategy;
    }

    /**
     * 根据用户的输入到数据库中查找地点名称、位置、判断地点类型，补全导航计算所需信息
     * @param place 一个地点的名称/经纬度/地点ID，即API接受的输入值
     * @return 一个navigatePlace实例，包含名称、经纬度、地点类型，方便内部计算时调用
     */
    public navigatePlace parsePlace(String place){
        navigatePlace result = new navigatePlace();
        Pattern PKpattern = Pattern.compile("(PK)([0-9]*)"); // regexp匹配数字ID
        Pattern DTpattern = Pattern.compile("(DT)([0-9]*)"); // regexp匹配数字ID
        Matcher PKmatcher = PKpattern.matcher(place);
        Matcher DTmatcher = DTpattern.matcher(place);
        if (PKmatcher.find()){
            place = PKmatcher.group(1);
            Optional<MapVertexInfo> vtx_record = mapVertexInfoRepository
                    .findById(Integer.parseInt(place));
            if (vtx_record.isPresent()) { // input is a parkingPlace
                result.setLocation(vtx_record.get().getLocation());
                result.setPlaceName(vtx_record.get().getVertexName());
                result.setPlaceType(navigatePlace.PlaceType.parking);
                return result;
            }
        }
        if (DTmatcher.find()){
            place = DTmatcher.group(1);
            Optional<Destination> dst_record = destinationRepository
                    .findById(Integer.parseInt(place));
            if (dst_record.isPresent()){ // input is a destination
                result.setLocation(dst_record.get().getLocation());
                result.setPlaceName(dst_record.get().getPlaceName());
                result.setPlaceType(navigatePlace.PlaceType.destination);
                return result;
            }
        }
        try { // 读取坐标
            Point loc = (Point) new WKTReader().read(place);
            result.setLocation(loc);
            result.setPlaceName(loc.toString()+"附近的位置");
            result.setPlaceType(navigatePlace.PlaceType.point);
            return result;
        } catch (ParseException e) { // 调用外部地图API定位
            Map<String,String> params=new HashMap<>();
            params.put("keyword",place);
            params.put("boundary","rectangle(31.016309,121.423743,31.033088,121.449057)");
            params.put("key","I6IBZ-BCZRI-FHYGG-523D4-3W3C7-X6BRS");
            params.put("page_index","1");
            params.put("page_size","1");
            ResponseEntity<PlaceResponse> tencentResponse =
                    restTemplate.getForEntity("https://apis.map.qq.com/ws/place/v1/search?keyword={keyword}" +
                                    "&boundary={boundary}&key={key}&page_index={page_index}&page_size={page_size}",
                            PlaceResponse.class,params);
            // TODO: error code + NULL?
            result.setLocation(Objects.requireNonNull(tencentResponse.getBody()).getLocation());
            result.setPlaceName(Objects.requireNonNull(tencentResponse.getBody()).getTitle());
            result.setPlaceType(navigatePlace.PlaceType.point);
            return result;
        }
    }
}

