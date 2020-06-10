package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.sjtugo.api.DAO.*;
import org.sjtugo.api.DAO.Entity.CarInfo;
import org.sjtugo.api.DAO.Entity.Destination;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.Entity.HelloBikeInfo;
import org.sjtugo.api.controller.ResponseEntity.MapVertexResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class MapInfoService {
    private final MapVertexInfoRepository mapVertexInfoRepository;
    private final DestinationRepository destinationRepository;
    private final HelloBikeRepository helloBikeRepository;
    private final CarInfoRepository carInfoRepository;
    private final RestTemplate restTemplate;

    public MapInfoService(MapVertexInfoRepository mapVertexInfoRepository,
                          DestinationRepository destinationRepository,
                          HelloBikeRepository helloBikeRepository,
                          CarInfoRepository carInfoRepository,
                          RestTemplate restTemplate){
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.destinationRepository = destinationRepository;
        this.helloBikeRepository = helloBikeRepository;
        this.carInfoRepository = carInfoRepository;
        this.restTemplate = restTemplate;
    }

    public List<MapVertexInfo> searchParkingSimple(String keyword) {
        return mapVertexInfoRepository.findByVertexNameLike("%"+keyword+"%");
    }

    public List<MapVertexResponse> searchParking(String keyword) {
        return mapVertexInfoRepository.findByVertexNameLike("%"+keyword+"%")
                .stream()
                .map(this::dataToResponse)
                .collect(Collectors.toList());
    }

    public List<Destination> searchPlace(String keyword) {
        return destinationRepository.findByPlaceNameLike("%"+keyword+"%");
    }

    public int getCurrentBike(int parkID){
        return helloBikeRepository.findBikeCounts(String.valueOf(parkID));
    }

    public int getCurrentMotor(int parkID){
        return 0; // TODO
    }

    public List<HelloBikeInfo> nearbyBikes (double lng,double lat){
        return helloBikeRepository.findNearbyBikes(lng,lat);
        //        return helloBikeRepository.findAllByLngBetweenAndLatBetweenAndTimeAfter
//                (lng-0.002, lng+0.002,
//                lat -0.002, lat+0.002, LocalDateTime.now().minusMinutes(2));
    }

    public List<CarInfo> nearbyCars (){
        return carInfoRepository.findCurrentCars();
        //        return helloBikeRepository.findAllByLngBetweenAndLatBetweenAndTimeAfter
//                (lng-0.002, lng+0.002,
//                lat -0.002, lat+0.002, LocalDateTime.now().minusMinutes(2));
    }




    public List<MapVertexResponse> nearbyParking(Point a){
        Polygon window = nearbyWindow(a);
        return mapVertexInfoRepository.findNearbyPoint(window)
                .stream()
                .map(this::dataToResponse)
                .collect(Collectors.toList());
    }




    /**
     * 根据数据库查询结果，查询实时单车数
     * @param info 传入数据库对象
     */
    public MapVertexResponse dataToResponse (MapVertexInfo info) {
        MapVertexResponse result = new MapVertexResponse();
        result.setVertexInfo (info);
        result.setBikeCount(getCurrentBike(info.getVertexID()));
        result.setMotorCount(getCurrentMotor(info.getVertexID()));
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
    public List<HelloBikeInfo> nearbyMobikes(double lat, double lng) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3 like Mac OS X)" +
                        " AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148" +
                        " MicroMessenger/7.0.12(0x17000c2d) NetType/WIFI Language/zh_CN");
        headers.set("accesstoken","AxA4yAbjw1EdFWWVSGvfok-OQlYAAAAArQkAAB7QZ3M3hdaIzPvFSD7zx5NRbIKmbum_PbdPpD4hP9L8SvQHi7uSQajodVjL9724VQ");
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("userid","1899972877305856249090");
        params.add("latitude", String.valueOf(lat));
        params.add("longitude",String.valueOf(lng));
        params.add("citycode","021");
        params.add("biketype","0");
        params.add("errMsg","getMapCenterLocation:ok");

        HttpEntity<Object> request = new HttpEntity<>(params,headers);

        LinkedHashMap<String,Object> mobikeResponse =
                restTemplate.postForObject("https://mwx.mobike.com/nearby/nearbyBikeInfo",request,LinkedHashMap.class);
        assert mobikeResponse != null;

//        System.out.println(mobikeResponse.get("object"));
//        System.out.println(((List<LinkedHashMap<String,Object>>) mobikeResponse.get("object")
//        ).size());

        return (((List<LinkedHashMap<String,Object>>) mobikeResponse.get("object"))
                .stream().map(bikeInfo -> new HelloBikeInfo((String) bikeInfo.get("bikeIds"),
                        (double) bikeInfo.get("distX"), (double) bikeInfo.get("distY"),
                        (int) bikeInfo.get("biketype")))
                .collect(Collectors.toList()));
    }
}
