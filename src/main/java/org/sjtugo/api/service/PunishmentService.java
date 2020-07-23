package org.sjtugo.api.service;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.TripRepository;
import org.sjtugo.api.entity.*;
import org.sjtugo.api.DAO.PunishmentRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity<?> addpunishment(List<List<Double>> punishment){
        List<TimeStamp> timcomputed = new ArrayList<TimeStamp>();
        List<TimeStamp> timres = new ArrayList<TimeStamp>();
        List<TimeStamp> timfilter = new ArrayList<TimeStamp>();
        for (int j = 0; j < punishment.size(); j++){
            int timest = (int) Math.round((punishment.get(j)).get(3));
            TimeStamp timeStamp = new TimeStamp();
            timeStamp.setStamp(timest);
            List<MapVertexInfo> vertexlis1 = mapVertexInfoRepository.findNearest((double) (punishment.get(j)).get(0), (double) (punishment.get(j)).get(1));
            int verid =vertexlis1.get(0).getVertexID();
            timeStamp.setVertexid(verid);
            timres.add(timeStamp);
        }
        for (int j=0; j<timcomputed.size();j++){
            for(int i=0; i < timres.size();i++) {
                if (timres.get(i)==timcomputed.get(j)){timfilter.add(timres.get(i));break;}
                if (i == timres.size()-1){return new ResponseEntity<>("userInfo", HttpStatus.OK);}
            }
        }
        for (int j = 0; j < punishment.size()-1; j++){
            int speed = (int) Math.round((punishment.get(j)).get(3));
            if (speed>0) {
                Punishment pus = new Punishment();
                List<MapVertexInfo> vertexlist1 = mapVertexInfoRepository.findNearest((double) (punishment.get(j)).get(0), (double) (punishment.get(j)).get(1));
                int depart =vertexlist1.get(0).getVertexID();
                List<MapVertexInfo> vertexlist2 = mapVertexInfoRepository.findNearest((double) (punishment.get(j+1)).get(0), (double) (punishment.get(j+1)).get(1));
                int arrive =vertexlist2.get(0).getVertexID();
                pus.setType(0);
                LocalDateTime loc;
                loc = LocalDateTime.of(2018, 1, 1, 20, 31, 20);
                pus.setTime(loc);
                pus.setVertexDepart(depart);
                pus.setVertexArrive(arrive);
                punishmentRepository.save(pus);
            }
        }


        return new ResponseEntity<>("userInfo", HttpStatus.OK);
    }


    private static final List<String> recordTrafficType = Arrays.asList("BIKE", "E100", "MOTOR");

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