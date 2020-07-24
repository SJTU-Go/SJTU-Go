package org.sjtugo.api.service;
import net.sf.json.JSONArray;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;


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

import javax.validation.constraints.Null;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> addpunishment(List<List<Double>> punishment,int tripid,int beginRouteTime, int type,int segindex){
        Trip usedtrip = tripRepository.findByTripID(tripid);
        //待完成
        int Typ = 0;
        List<TimeStamp> timecop = this.findTripExpectTime(tripid,segindex,type);
        List<TimeStampMerge> timecomputed = new ArrayList<TimeStampMerge>();
        Integer timerecorded = beginRouteTime;
        //已完成
        List<TimeStamp> timres = new ArrayList<TimeStamp>();
        List<TimeStamp> timfilter = new ArrayList<TimeStamp>();
        List<TimeStampMerge> timMerge = new ArrayList<TimeStampMerge>();


        for (int j = 0; j < punishment.size(); j++){
            int timest = (int) Math.round((punishment.get(j)).get(3));
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setStamp(timest);
            List<MapVertexInfo> vertexlis1 = mapVertexInfoRepository.findNearest((double) (punishment.get(j)).get(0), (double) (punishment.get(j)).get(1));
            int verid =vertexlis1.get(0).getVertexID();
            timeStamp.setVertexid(verid);
            timres.add(timeStamp);
        }

        for (int j=0; j<timecop.size();j++){
            for(int i=0; i < timres.size();i++) {
                if ((timres.get(i).getVertexid()==timecop.get(j).getVertexid()))
                    {timfilter.add(timres.get(i));break;}
                if (i == timres.size()-1){return new ResponseEntity<>("userInfo", HttpStatus.OK);}
            }
        }

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
            loc = LocalDateTime.parse(date);
            pus.setTime(loc);
            punishmentRepository.save(pus);
        }

        return new ResponseEntity<>("userInfo", HttpStatus.OK);
    }

    private static final List<String> recordTrafficType = Arrays.asList("BIKE", "E100", "MOTOR");

    @SuppressWarnings("unchecked")
    private List<TimeStamp> findTripExpectTime(Integer tripID, Integer segIndex,Integer type){
        Trip curTrip = tripRepository.findById(tripID).orElseThrow();
        List<List<Integer>> curRoutes = ((List<LinkedHashMap<String,Object>>) curTrip.getStrategy().get("routeplan"))
                .stream().filter(item -> recordTrafficType.contains((String) item.get("type")))
                .map(item -> (List<Integer>) item.get("passingVertex")).collect(Collectors.toList());

        List<Integer> curRoute = curRoutes.get(segIndex);
        List<TimeStamp> result = new ArrayList<>();
        int stamp = 0;
        for (int j = 0; j < curRoute.size()-1; j++) {
            TimeStamp tmpStamp = new TimeStamp();
            stamp += findEdgeTime(curRoute.get(j), curRoute.get(j + 1),type);
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