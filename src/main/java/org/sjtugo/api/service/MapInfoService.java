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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

    public List<MapVertexInfo> searchParking(String keyword) {
        return mapVertexInfoRepository.findByVertexNameLike("%"+keyword+"%")
                .stream()
                .peek(v-> v.setVertexName(v.getVertexName()+"停车点"))
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




    public List<MapVertexInfo> nearbyParking(Point a){
        return mapVertexInfoRepository.findNearbyPoint(a.getX(),a.getY())
                .stream()
                .peek(v-> v.setVertexName(v.getVertexName()+"停车点"))
                .collect(Collectors.toList());
    }






//    private Polygon nearbyWindow(Point t){
//        double lng_shift = 0.005;
//        double lat_shift = 0.005;
//        double x = t.getCoordinate().x;
//        double y = t.getCoordinate().y;
//        return new GeometryFactory().createPolygon(
//                new Coordinate[]{
//                        new Coordinate(x - lng_shift, y - lat_shift),
//                        new Coordinate(x-lng_shift,y+lat_shift),
//                        new Coordinate(x+lng_shift,y+lat_shift),
//                        new Coordinate(x+lng_shift,y-lat_shift),
//                        new Coordinate(x - lng_shift, y - lat_shift)
//                });
//    }

    @SuppressWarnings("unchecked")
    public List<HelloBikeInfo> nearbyMobikes(double lat, double lng) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Host", "app2.mobike.com");
        headers.set("User-Agent", "com.mobike.bike/189 (unknown, iOS 13.3, iPhone, Scale/2.000000)");
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("bikenum","100");
        params.add("biketype","0");
        params.add("citycode","021");
        params.add("client_id","ios");
        params.add("filterMode","0");
        params.add("latitude",String.valueOf(lat));
        params.add("longitude",String.valueOf(lng));
        params.add("scope","500");

        HttpEntity<Object> request = new HttpEntity<>(params,headers);

        LinkedHashMap<String,Object> mobikeResponse =
                restTemplate.postForObject("https://app2.mobike.com/api/nearby/v4/nearbyBikeInfo",request,LinkedHashMap.class);
        assert mobikeResponse != null;

//        System.out.println(mobikeResponse.values());
//        System.out.println(mobikeResponse.get("object"));
//        System.out.println(((List<LinkedHashMap<String,Object>>) mobikeResponse.get("object")
//        ).size());

        return (((List<LinkedHashMap<String,Object>>) mobikeResponse.get("bike"))
                .stream().map(bikeInfo -> new HelloBikeInfo((String) bikeInfo.get("distId"),
                        (double) bikeInfo.get("distX"), (double) bikeInfo.get("distY"),
                        (int) bikeInfo.get("biketype")))
                .collect(Collectors.toList()));
    }
}
