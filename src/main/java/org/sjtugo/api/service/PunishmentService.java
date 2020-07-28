package org.sjtugo.api.service;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import net.sf.json.JSONObject;

import org.sjtugo.api.entity.Punishment;
import org.sjtugo.api.entity.TimeStamp;
import org.sjtugo.api.entity.Trip;
import org.sjtugo.api.entity.TimeStampMerge;
import org.sjtugo.api.DAO.PunishmentRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PunishmentService {
    private final PunishmentRepository punishmentRepository;
    private final MapVertexInfoRepository mapVertexInfoRepository;
    private final TripRepository tripRepository;
    private final RestTemplate restTemplate;

    public PunishmentService(MapVertexInfoRepository mapVertexInfoRepository, PunishmentRepository punishmentRepository, TripRepository tripRepository, RestTemplate restTemplate){
        this.punishmentRepository = punishmentRepository;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.tripRepository = tripRepository;
        this.restTemplate = restTemplate;
    }
    public ResponseEntity<?> addpunishment(List<List<Double>> punishment,int tripid,long beginRouteTime, int type,int segindex){
        Trip usedtrip = tripRepository.findByTripID(tripid);
        //待完成
        int Typ = 0;
        List<TimeStamp> timecop = this.findTripExpectTime(tripid,segindex,type);
        List<TimeStampMerge> timecomputed = new ArrayList<TimeStampMerge>();
        Long timerecorded = beginRouteTime;
        //已完成
        List<TimeStamp> timres = new ArrayList<TimeStamp>();
        List<TimeStamp> timfilter = new ArrayList<TimeStamp>();
        List<TimeStampMerge> timMerge = new ArrayList<TimeStampMerge>();
//        System.out.println("cop");
//        System.out.println(timecop);

        for (int j = 0; j < punishment.size(); j++){
//            System.out.println("pun");
//            System.out.println(punishment.get(j));
            int timest = (int) Math.round((punishment.get(j)).get(3));
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setStamp(timest);
//            System.out.println((mapVertexInfoRepository.findNearest((double) (punishment.get(j)).get(0), (double) (punishment.get(j)).get(1))).get(0));
            int verid = ((mapVertexInfoRepository.findNearest((double) (punishment.get(j)).get(0), (double) (punishment.get(j)).get(1))).get(0)).getVertexID();
            //int verid =vertexlis1.get(0).getVertexID();
            timeStamp.setVertexid(verid);
            timres.add(timeStamp);
        }
//        System.out.println("res");
//        System.out.println(timres);
        for (int j=0; j<timecop.size();j++){
            for(int i=0; i < timres.size();i++) {
                if ((timres.get(i).getVertexid().equals(timecop.get(j).getVertexid())))
                    {timfilter.add(timres.get(i));break;}
                if (i == timres.size()-1){return new ResponseEntity<>("userInfo", HttpStatus.OK);}
            }
        }
//        System.out.println("filter");
//        System.out.println(timfilter);
        for (int j=0; j<timfilter.size()-1;j++) {
            TimeStampMerge timstmerge = new TimeStampMerge();
            timstmerge.setVertexid1(timfilter.get(j).getVertexid());
            timstmerge.setVertexid2(timfilter.get(j+1).getVertexid());
            timstmerge.setStampgap(timfilter.get(j+1).getStamp()-timfilter.get(j).getStamp());
            timMerge.add(timstmerge);
        }

        for (int j=0; j<timecop.size()-1;j++) {
            TimeStampMerge timstmerge = new TimeStampMerge();
            timstmerge.setVertexid1(timecop.get(j).getVertexid());
            timstmerge.setVertexid2(timecop.get(j+1).getVertexid());
            timstmerge.setStampgap(timecop.get(j+1).getStamp()-timecop.get(j).getStamp());
            timecomputed.add(timstmerge);

        }

        for (int j=0; j<timMerge.size();j++){

            Punishment pus = new Punishment();
            if(timMerge.get(j).getStampgap()<timecomputed.get(j).getStampgap()){pus.setPunish(-1);}
            if(timMerge.get(j).getStampgap().equals(timecomputed.get(j).getStampgap())){pus.setPunish(0);}
            if(timMerge.get(j).getStampgap()>timecomputed.get(j).getStampgap()){pus.setPunish(1);}
            pus.setVertexDepart(timMerge.get(j).getVertexid1());
            pus.setVertexArrive(timMerge.get(j).getVertexid2());
            pus.setType(type);
            timerecorded = timerecorded +timMerge.get(j).getStampgap();
            LocalDateTime loc;
            String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timerecorded * 1000));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            loc = LocalDateTime.parse(date, formatter);
            pus.setTime(loc);
            punishmentRepository.save(pus);
        }

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    private static final List<String> recordTrafficType = Arrays.asList("HELLOBIKE", "E100", "MOTOR");

    @SuppressWarnings("unchecked")
    private List<TimeStamp> findTripExpectTime(Integer tripID, Integer segIndex,Integer type){
        Trip curTrip = tripRepository.findById(tripID).orElseThrow();
        List<List<String>> curRoutes = ((List<JSONObject>) curTrip.getStrategy().get("routeplan"))
                .stream().filter(item -> recordTrafficType.contains(item.get("type")))
                .map(item -> (List<String>) item.get("passingVertex")).collect(Collectors.toList());
        List<String> curRoute = curRoutes.get(segIndex);
//        System.out.println(curRoute.get(0));
//        System.out.println(curRoute.get(0).getClass());
        List<TimeStamp> result = new ArrayList<>();
        int stamp = 0;
        for (int j = 0; j < curRoute.size()-1; j++) {
            TimeStamp tmpStamp = new TimeStamp();
            stamp += findEdgeTime(curRoute.get(j), curRoute.get(j + 1),type);
            // TODO: updateType
            tmpStamp.setStamp(stamp);
            tmpStamp.setVertexid(Integer.valueOf(curRoute.get(j)));
            result.add(tmpStamp);
        }
        return result;
    }
    @SuppressWarnings("unchecked")
    private Double findEdgeTime(String startID, String endID, Integer updateType){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjEuNTk1NTcwNzkyODQ4MDg0ZSs2LCJleHAiOjE1OTgxNjI3OTIsImlzcyI6ImFyYW5nb2RiIiwicHJlZmVycmVkX3VzZXJuYW1lIjoicm9vdCJ9.oi9cVga6WYD8EprNyzdlwWcXv7pzuKbmOaClUaD6nHU=");

        String query = "";
        switch (updateType){
            case 0:
                query = "FOR edge IN bikeedge\n" +
                        "FILTER edge._from == \"vertex/" + startID +"\"\n" +
                        "AND edge._to == \"vertex/" + endID +"\"\n" +
                        "COLLECT t = edge.normalBikeTime\n" +
                        "RETURN t";
                break;
            case 1:
                query = "FOR edge IN bikeedge\n" +
                        "FILTER edge._from == \"vertex/" + startID +"\"\n" +
                        "AND edge._to == \"vertex/" + endID +"\"\n" +
                        "COLLECT t = edge.normalMotorTime\n" +
                        "RETURN t";
                break;
            case 2:
                query = "FOR edge IN caredge\n" +
                        "FILTER edge._from == \"vertex/" + startID +"\"\n" +
                        "AND edge._to == \"vertex/" + endID +"\"\n" +
                        "COLLECT t = edge.normalCarTime\n" +
                        "RETURN t";
            default:
                break;
        }

        Map<String,Object> bindVars = new HashMap<>();
        bindVars.put("query", query);
        bindVars.put("batchSize", 1);

        HttpEntity<Object> update_request = new HttpEntity<>(bindVars,headers);
        Map<String,Object> result = (Map<String, Object>) restTemplate
                .postForObject("http://47.92.147.237:8529/_api/cursor",
                        update_request,Object.class);
        return ((List<Double>) result.get("result")).get(0);
    }

    @SuppressWarnings("unchecked")
    private List<TimeStamp> findTripExpectTime(Integer tripID, Integer segIndex){
        Trip curTrip = tripRepository.findById(tripID).orElseThrow();
        List<List<Integer>> curRoutes = ((List<LinkedHashMap<String,Object>>) curTrip.getStrategy().get("routePlan"))
                                .stream().filter(item -> recordTrafficType.contains((String) item.get("type")))
                                .map(item -> (List<Integer>) item.get("passingVertex")).collect(Collectors.toList());

        List<Integer> curRoute = curRoutes.get(segIndex);
        List<TimeStamp> result = new ArrayList<>();
        int stamp = 0;
        for (int j = 0; j < curRoute.size()-1; j++) {
            TimeStamp tmpStamp = new TimeStamp();
            stamp += findEdgeTime(curRoute.get(j), curRoute.get(j + 1), 0);
            // TODO: updateType
            tmpStamp.setStamp(stamp);
            tmpStamp.setVertexid(curRoute.get(j));
            result.add(tmpStamp);
        }
        return result;
    }

    private Double findEdgeTime(Integer startID, Integer endID, Integer updateType){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjEuNTkxNTI5OTc2NDQ5Nzc1M2UrNiwiZXhwIjoxNTk0MTIxOTc2LCJpc3MiOiJhcmFuZ29kYiIsInByZWZlcnJlZF91c2VybmFtZSI6InJvb3QifQ==.UElwRx6Iy9yvT2gvX2rdCjlLnc73E56RfV6hEQd1sLA=");

        String query = "";
        switch (updateType){
            case 0:
                query = "FOR edge IN bikeedge\n" +
                        "FILTER edge._from == \"vertex/" + startID.toString() +"\"\n" +
                        "AND edge._to == \"vertex/" + endID.toString() +"\"\n" +
                        "COLLECT t = edge.normalBikeTime\n" +
                        "RETURN t";
                break;
            case 1:
                query = "FOR edge IN bikeedge\n" +
                        "FILTER edge._from == \"vertex/" + startID.toString() +"\"\n" +
                        "AND edge._to == \"vertex/" + endID.toString() +"\"\n" +
                        "COLLECT t = edge.normalMotorTime\n" +
                        "RETURN t";
                break;
            case 2:
                query = "FOR edge IN caredge\n" +
                        "FILTER edge._from == \"vertex/" + startID.toString() +"\"\n" +
                        "AND edge._to == \"vertex/" + endID.toString() +"\"\n" +
                        "COLLECT t = edge.normalCarTime\n" +
                        "RETURN t";
            default:
                break;
        }

        Map<String,Object> bindVars = new HashMap<>();
        bindVars.put("query", query);
        bindVars.put("batchSize", 1);

        HttpEntity<Object> update_request = new HttpEntity<>(bindVars,headers);
        Map<String,Object> result = (Map<String, Object>) restTemplate
                                    .postForObject("http://47.92.147.237:8529/_api/cursor",
                                                    update_request,Object.class);
        return ((List<Double>) result.get("result")).get(0);
    }
}