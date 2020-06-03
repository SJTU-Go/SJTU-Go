package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTReader;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.controller.ResponseEntity.TrafficInfoResponse;
import org.sjtugo.api.entity.BikeRoute;
import org.sjtugo.api.entity.TrafficInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TrafficService {

    protected final RestTemplate restTemplate;
    protected final TrafficInfoRepository trafficInfoRepository;

    public TrafficService(RestTemplate restTemplate, TrafficInfoRepository trafficInfoRepository) {
        this.restTemplate = restTemplate;
        this.trafficInfoRepository = trafficInfoRepository;
    }

    public void newTraffic(TrafficInfo trafficInfo) {
        trafficInfoRepository.save(trafficInfo);
        scheduleArango(trafficInfo);
    }


    private void scheduleArango(TrafficInfo task) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,Object> bindVars = new HashMap<>();
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor("public", ""));

        bindVars.put("id","update"+task.getTrafficID());
        bindVars.put("name","update"+task.getMessage().substring(0,10));
        if (task.getRepeat() > 0){
            bindVars.put("repeat",task.getRepeat());
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
                "} (params)");
        Map<String,Object> update_params = new HashMap<>();
        update_params.put("relatedVertex",task.getRelatedVertex());
        update_params.put("motorSpeed",task.getMotorSpeed());
        update_params.put("bikeSpeed",task.getBikeSpeed());
        update_params.put("carSpeed",task.getCarSpeed());
        bindVars.put("params",update_params);

        HttpEntity<Object> update_request = new HttpEntity<>(bindVars,headers);
        restTemplate.postForObject("http://47.92.147.237:8529/_api/tasks",
                        update_request,Integer.class);

        // restore traffic
        bindVars.replace("id","restore"+task.getTrafficID());
        bindVars.replace("name","restore"+task.getMessage().substring(0,10));
        bindVars.replace("offset", Duration.between(LocalDateTime.now(),
                task.getEndTime().atDate(task.getBeginDay())).toSeconds());
        Map<String,Object> restore_params = new HashMap<>();
        restore_params.put("relatedVertex",task.getRelatedVertex());
        bindVars.replace("params",restore_params);
        bindVars.replace("command","var carspeed = 16;\n" +
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
                "}");
        HttpEntity<Object> restore_request = new HttpEntity<>(bindVars,headers);
        restTemplate.postForObject("http://47.92.147.237:8529/_api/tasks",
                restore_request,Integer.class);
        }


}
