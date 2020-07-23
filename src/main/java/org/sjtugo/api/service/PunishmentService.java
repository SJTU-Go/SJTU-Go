package org.sjtugo.api.service;
import net.sf.json.JSONArray;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.entity.Punishment;
import org.sjtugo.api.entity.TimeStamp;
import org.sjtugo.api.DAO.PunishmentRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.entity.User;
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
    public PunishmentService(MapVertexInfoRepository mapVertexInfoRepository,PunishmentRepository punishmentRepository){
        this.punishmentRepository = punishmentRepository;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
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
}