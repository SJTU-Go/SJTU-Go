package org.sjtugo.api.service;

import org.sjtugo.api.DAO.HistoryRepository;
import org.sjtugo.api.entity.History;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository){
        this.historyRepository = historyRepository;
    }

    public ResponseEntity<?> addHistory(Integer userID, String depart, String arrive,
                                        String routetime, String route){
        History newHistory = new History();
        newHistory.setUserID(userID);
        newHistory.setDepart(depart);
        newHistory.setArrive(arrive);
        newHistory.setRoutetime(routetime);
        newHistory.setRoute(route);
        historyRepository.save(newHistory);
        return new ResponseEntity<>(newHistory, HttpStatus.OK);
    }

    public List<History> getHistoryList(Integer userID){
        return historyRepository.findByUserID(userID);
    }
}

