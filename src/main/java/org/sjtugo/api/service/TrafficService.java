package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.controller.ResponseEntity.TrafficInfoResponse;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class TrafficService {
    protected final RestTemplate restTemplate;
    protected final TrafficInfoRepository trafficInfoRepository;
    protected final MapVertexInfoRepository mapVertexInfoRepository;
    private String arangoAuthKey;

    private static final double normalCarSpeed = 8;
    private static final double normalBikeSpeed = 2.5;
    private static final double normalMotorSpeed = 5.5;

    public TrafficService(RestTemplate restTemplate, TrafficInfoRepository trafficInfoRepository, MapVertexInfoRepository mapVertexInfoRepository, String arangoAuthKey) {
        this.restTemplate = restTemplate;
        this.trafficInfoRepository = trafficInfoRepository;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.arangoAuthKey = arangoAuthKey;
    }

    public ErrorResponse newTraffic(TrafficInfo trafficInfo) {
        trafficInfoRepository.save(trafficInfo);
        ErrorResponse arangoResponse = scheduleArango(trafficInfo);
        if (arangoResponse.getCode() == 0) {
            return new ErrorResponse(0,"修改Arango交通信息成功");
        }
        else {
            trafficInfoRepository.delete(trafficInfo);
            return arangoResponse;
        }
    }

    /** 删除trafficRepository中的信息，并且删除Arango中对应ID的task
     * @param id TrafficID
     * @return 成功或错误提示
     */
    public ErrorResponse deleteTraffic(Integer id) {
        Optional<TrafficInfo> todelete = trafficInfoRepository.findById(id);
        if (todelete.isEmpty()) {
            return new ErrorResponse(4, "Traffic Info by id Not Found in Repository");
        }
        ErrorResponse arangoResponse = removeArango(todelete.get());
        if (arangoResponse.getCode() == 0) {
            trafficInfoRepository.deleteById(id);
            return new ErrorResponse(0,"删除Arango交通信息成功");
        } else{
            return new ErrorResponse(4,"删除traffic数据库失败或，"+arangoResponse.getMessage());
        }
    }

    
    public List<TrafficInfoResponse> currentTraffic() {
        return trafficInfoRepository
                .findAllByBeginTimeIsBeforeAndEndTimeIsAfter(LocalTime.now(),LocalTime.now())
                .stream().filter(traffic ->
                        traffic.getRepeatTime() == 0 && LocalDate.now().isEqual(traffic.getBeginDay()) ||
                        traffic.getRepeatTime() != 0 &&
                        Period.between(traffic.getBeginDay(), LocalDate.now()).getDays()
                                % traffic.getRepeatTime() == 0)
                .map(this::makeResponse)
                .collect(Collectors.toList());
    }

    private ErrorResponse scheduleArango(TrafficInfo task) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization",arangoAuthKey);
//        System.out.println(headers);
        Map<String,Object> bindVars = new HashMap<>();
        bindVars.put("id","update"+task.getTrafficID());
        bindVars.put("name","update"+task.getName());
        if (task.getRepeatTime() > 0){
            bindVars.put("period",86400*task.getRepeatTime());
        }

        if (task.getBeginDay().isBefore(LocalDate.now())) {
            return new ErrorResponse(3,"invalid beginDay!");
        } else if (task.getBeginTime().atDate(task.getBeginDay()).isBefore(LocalDateTime.now())) {
            return new ErrorResponse(3,"invalid beginTime!");
        } else if (task.getEndTime().isBefore(task.getBeginTime())) {
            return new ErrorResponse(3,"invalid endTime!");
        }



        // update traffic
        bindVars.put("offset", Duration.between(LocalDateTime.now(),
                task.getBeginTime().atDate(task.getBeginDay())).toSeconds());
        bindVars.put("command","  (function(params) {var arrayLength = params.relatedVertex.length;\n" +
                "    var db = require('@arangodb').db;\n" +
                "    for (var i = 0; i < arrayLength - 1; i++) { \n" +
                "        var caredge = db.caredge.byExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}).toArray(); \n" +
                "        if (caredge.length != 0) { \n" +
                "            var distance = caredge[0].distance; \n" +
                "        } \n" +
                "        var newcartime = distance/params.carspeed;\n" +
                "        db.caredge.updateByExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}, {avoidCarTime : 888888888, normalCarTime : newcartime }); \n" +
                "        var bikeedge = db.bikeedge.byExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}).toArray(); \n" +
                "        if (bikeedge.length != 0) { \n" +
                "            var distance = bikeedge[0].distance; \n" +
                "        }\n" +
                "        var newmotortime = distance/params.motorspeed;\n" +
                "        var newbiketime = distance/params.bikespeed;\n" +
                "        db.bikeedge.updateByExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}, {avoidBikeTime : 888888888, normalBikeTime : newbiketime, avoidMotorTime : 888888888, normalMotorTime : newmotortime });\n" +
                "    }}) (params)");
        Map<String,Object> update_params = new HashMap<>();
        update_params.put("relatedVertex",task.getRelatedVertex());
        update_params.put("motorspeed",task.getMotorSpeed());
        update_params.put("bikespeed",task.getBikeSpeed());
        update_params.put("carspeed",task.getCarSpeed());
        bindVars.put("params",update_params);

//        System.out.println(bindVars);

        HttpEntity<Object> update_request = new HttpEntity<>(bindVars,headers);
        restTemplate.put("http://47.92.147.237:8529/_api/tasks/"+bindVars.get("id"),
                        update_request);

        // restore traffic
        bindVars.replace("id","restore"+task.getTrafficID());
        bindVars.replace("name","restore"+task.getName());
        bindVars.replace("offset", Duration.between(LocalDateTime.now(),
                task.getEndTime().atDate(task.getBeginDay())).toSeconds());
        Map<String,Object> restore_params = new HashMap<>();
        restore_params.put("relatedVertex",task.getRelatedVertex());
        restore_params.put("motorspeed",normalMotorSpeed);
        restore_params.put("bikespeed",normalBikeSpeed);
        restore_params.put("carspeed",normalCarSpeed);
        bindVars.replace("params",restore_params);
        bindVars.replace("command","(function(params) {var arrayLength = params.relatedVertex.length;\n" +
                "  var db = require('@arangodb').db;\n" +
                "  for (var i = 0; i < arrayLength - 1; i++) { \n" +
                "      var caredge = db.caredge.byExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}).toArray(); \n" +
                "      if (caredge.length != 0) { \n" +
                "          var distance = caredge[0].distance; \n" +
                "      } \n" +
                "      var newcartime = distance/params.carspeed;\n" +
                "      db.caredge.updateByExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}, {avoidCarTime : newcartime, normalCarTime : newcartime }); \n" +
                "      var bikeedge = db.bikeedge.byExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}).toArray(); \n" +
                "      if (bikeedge.length != 0) { \n" +
                "          var distance = bikeedge[0].distance; \n" +
                "      }\n" +
                "      var newmotortime = distance/params.motorspeed;\n" +
                "      var newbiketime = distance/params.bikespeed;\n" +
                "      db.bikeedge.updateByExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}, {avoidBikeTime : newbiketime, normalBikeTime : newbiketime, avoidMotorTime : newmotortime, normalMotorTime : newmotortime });\n" +
                "  }}) (params)");
        HttpEntity<Object> restore_request = new HttpEntity<>(bindVars,headers);
        restTemplate.put("http://47.92.147.237:8529/_api/tasks/"+bindVars.get("id"),
                restore_request);

        return new ErrorResponse(0,"修改Arango交通信息成功");
    }


    private TrafficInfoResponse makeResponse(TrafficInfo trafficInfo){
        TrafficInfoResponse response = new TrafficInfoResponse();
        response.setTrafficInfo(trafficInfo);
        Coordinate[] locations = trafficInfo.getRelatedVertex().stream()
                .map(id -> mapVertexInfoRepository.findById(id)
                        .orElseThrow().getLocation().getCoordinate())
                .toArray(Coordinate[]::new);
        response.setPointList( new GeometryFactory().createLineString(locations));
        return response;
    }

    private ErrorResponse removeArango(TrafficInfo traffic) {
        Integer trafficID = traffic.getTrafficID();
        MultiValueMap<String,String> headers = new HttpHeaders();
        headers.set("Authorization",arangoAuthKey);

        RestTemplate tempRestTemplate = new RestTemplateBuilder()
                .build();
        HttpEntity<?> delete_request = new HttpEntity<>(null,headers);
        try {
            tempRestTemplate.delete("http://47.92.147.237:8529/_api/tasks/update"+trafficID,
                    delete_request);
        } catch (Exception e) {
//            return new ErrorResponse(3,"删除UpdateArango交通Task失败");
        }

        try {
            tempRestTemplate.delete("http://47.92.147.237:8529/_api/tasks/restore"+trafficID,
                    delete_request);
        }catch (Exception e) {
//            return new ErrorResponse(3,"删除RestoreArango交通Task失败");
        }
        Map<String,Object> restore_params = new HashMap<>();
        Map<String,Object> collections_map = new HashMap<>();
        Map<String,Object> func_params = new HashMap<>();
        collections_map.put("write", new String[]{"bikeedge","caredge"});
        restore_params.put("collections",collections_map);
        func_params.put("motorspeed",normalMotorSpeed);
        func_params.put("bikespeed",normalBikeSpeed);
        func_params.put("carspeed",normalCarSpeed);
        func_params.put("relatedVertex",traffic.getRelatedVertex());
        restore_params.put("params",func_params);
        restore_params.put("action","(function(params) {var arrayLength = params.relatedVertex.length;\n" +
                        "  var db = require('@arangodb').db;\n" +
                        "  for (var i = 0; i < arrayLength - 1; i++) { \n" +
                        "      var caredge = db.caredge.byExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}).toArray(); \n" +
                        "      if (caredge.length != 0) { \n" +
                        "          var distance = caredge[0].distance; \n" +
                        "      } \n" +
                        "      var newcartime = distance/params.carspeed;\n" +
                        "      db.caredge.updateByExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}, {avoidCarTime : newcartime, normalCarTime : newcartime }); \n" +
                        "      var bikeedge = db.bikeedge.byExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}).toArray(); \n" +
                        "      if (bikeedge.length != 0) { \n" +
                        "          var distance = bikeedge[0].distance; \n" +
                        "      }\n" +
                        "      var newmotortime = distance/params.motorspeed;\n" +
                        "      var newbiketime = distance/params.bikespeed;\n" +
                        "      db.bikeedge.updateByExample({ _from: \"vertex/\" + params.relatedVertex[i], _to:\"vertex/\" + params.relatedVertex[i+1]}, {avoidBikeTime : newbiketime, normalBikeTime : newbiketime, avoidMotorTime : newmotortime, normalMotorTime : newmotortime });\n" +
                        "  }})"
                );
        HttpEntity<?> restore_request = new HttpEntity<>(restore_params,headers);
        try {
            JSONObject response = tempRestTemplate.postForObject("http://47.92.147.237:8529/_api/transaction",restore_request, JSONObject.class);
//            System.out.println(response);
//            return null;
            assert response != null;
            if ((Integer) response.get("code") == 200) {
                return new ErrorResponse(0,"Success");
            } else {
                throw new Exception();
            }
        }catch (Exception e) {
            return new ErrorResponse(3,"执行Restore任务失败");
        }
    }
}
