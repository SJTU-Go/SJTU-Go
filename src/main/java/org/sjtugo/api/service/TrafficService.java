package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.controller.ResponseEntity.TrafficInfoResponse;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class TrafficService {
    // TODO check 是否当前时间
    protected final RestTemplate restTemplate;
    protected final TrafficInfoRepository trafficInfoRepository;
    protected final MapVertexInfoRepository mapVertexInfoRepository;

    public TrafficService(RestTemplate restTemplate, TrafficInfoRepository trafficInfoRepository, MapVertexInfoRepository mapVertexInfoRepository) {
        this.restTemplate = restTemplate;
        this.trafficInfoRepository = trafficInfoRepository;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
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
        ErrorResponse arangoResponse = removeArango(id);
        if (arangoResponse.getCode() == 0) {
            trafficInfoRepository.deleteById(id);
            return new ErrorResponse(0,"删除Arango交通信息成功");
        } else{
            return new ErrorResponse(0,"删除traffic数据库失败，"+arangoResponse.getMessage());
        }
    }

    
    public List<TrafficInfoResponse> currentTraffic() {
        return trafficInfoRepository
                .findAllByBeginTimeIsBeforeAndEndTimeIsAfter(LocalTime.now(),LocalTime.now())
                .stream().filter(traffic ->
                        traffic.getRepeatTime() == 0 ||
                        Period.between(traffic.getBeginDay(), LocalDate.now()).getDays()
                                % traffic.getRepeatTime() == 0)
                .map(this::makeResponse)
                .collect(Collectors.toList());
    }

    private ErrorResponse scheduleArango(TrafficInfo task) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjEuNTkxNTI5OTc2NDQ5Nzc1M2UrNiwiZXhwIjoxNTk0MTIxOTc2LCJpc3MiOiJhcmFuZ29kYiIsInByZWZlcnJlZF91c2VybmFtZSI6InJvb3QifQ==.UElwRx6Iy9yvT2gvX2rdCjlLnc73E56RfV6hEQd1sLA=");
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
        bindVars.put("command","(function(params) {var arrayLength = relatedVertex.length;\n" +
                "for (var i = 0; i < arrayLength - 1; i++) {\n" +
                "    var caredge = db.caredge.byExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}).toArray();\n" +
                "    if (caredge.length != 0) {\n" +
                "        var distance = caredge[0].distance;\n" +
                "    }\n" +
                "    db.caredge.updateByExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}, {avoidCarTime : 999999999, normalCarTime : distance / carspeed });\n" +
                "\n" +
                "    var bikeedge = db.bikeedge.byExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}).toArray();\n" +
                "    if (bikeedge.length != 0) {\n" +
                "        var distance = bikeedge[0].distance;\n" +
                "    }\n" +
                "    db.bikeedge.updateByExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}, {avoidBikeTime : 999999999, normalBikeTime : distance / bikespeed, avoidMotorTime : 999999999, normalMotorTime : distance / motorspeed });\n" +
                "}}) (params)");
        Map<String,Object> update_params = new HashMap<>();
        update_params.put("relatedVertex",task.getRelatedVertex());
        update_params.put("motorSpeed",task.getMotorSpeed());
        update_params.put("bikeSpeed",task.getBikeSpeed());
        update_params.put("carSpeed",task.getCarSpeed());
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
        bindVars.replace("params",restore_params);
        bindVars.replace("command","(function(params) {var carspeed = 16;\n" +
                "var bikespeed = 6;\n" +
                "var motorspeed = 8;\n" +
                "var arrayLength = relatedVertex.length;\n" +
                "for (var i = 0; i < arrayLength - 1; i++) {\n" +
                "    var caredge = db.caredge.byExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}).toArray();\n" +
                "    if (caredge.length != 0) {\n" +
                "        var distance = caredge[0].distance;\n" +
                "    }\n" +
                "    db.caredge.updateByExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}, {avoidCarTime : distance / carspeed, normalCarTime : distance / carspeed });\n" +
                "\n" +
                "    var bikeedge = db.bikeedge.byExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}).toArray();\n" +
                "    if (bikeedge.length != 0) {\n" +
                "        var distance = bikeedge[0].distance;\n" +
                "    }\n" +
                "    db.bikeedge.updateByExample({ _from: \"vertex/\" + relatedVertex[i], _to:\"vertex/\" + relatedVertex[i+1]}, {avoidBikeTime : distance / bikespeed, normalBikeTime : distance / bikespeed, avoidMotorTime : distance / motorspeed, normalMotorTime : distance / motorspeed });\n" +
                "\n" +
                "}}) (params)");
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

    private ErrorResponse removeArango(Integer trafficID) {
        MultiValueMap<String,String> headers = new HttpHeaders();
        headers.set("Authorization","bearer "+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjEuNTkxNTI5OTc2NDQ5Nzc1M2UrNiwiZXhwIjoxNTk0MTIxOTc2LCJpc3MiOiJhcmFuZ29kYiIsInByZWZlcnJlZF91c2VybmFtZSI6InJvb3QifQ==.UElwRx6Iy9yvT2gvX2rdCjlLnc73E56RfV6hEQd1sLA=");


        RestTemplate tempRestTemplate = new RestTemplateBuilder()
                .basicAuthentication("root", "sjtugo")
                .build();
        HttpEntity<?> delete_request = new HttpEntity<>(null,headers);
        try {
            tempRestTemplate.delete("http://47.92.147.237:8529/_api/tasks/update"+trafficID,
                    delete_request);
        } catch (Exception e) {
            return new ErrorResponse(3,"删除UpdateArango交通信息失败");
        }

        try {
            tempRestTemplate.delete("http://47.92.147.237:8529/_api/tasks/restore"+trafficID,
                    delete_request);
            return new ErrorResponse(0,"Success");
        }catch (Exception e) {
            return new ErrorResponse(3,"删除RestoreArango交通信息成功");
        }

    }
}
