package org.sjtugo.api.service.NavigateService;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.SearchHistoryRepository;
import org.sjtugo.api.controller.RequestEntity.NavigateRequest;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class SearchHistory {
    private final SearchHistoryRepository searchHistoryRepository;
    private final MapVertexInfoRepository mapVertexInfoRepository;

    public SearchHistory(SearchHistoryRepository searchHistoryRepository, MapVertexInfoRepository mapVertexInfoRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
    }

    public ResponseEntity<?> startNavigation(NavigateRequest navigateRequest) throws ParseException {
        org.sjtugo.api.DAO.Entity.SearchHistory searHis = new org.sjtugo.api.DAO.Entity.SearchHistory();
        searHis.setSearchTime(LocalDateTime.now());
        String beginPlace = navigateRequest.getBeginPlace();
        String arrivePlace = navigateRequest.getArrivePlace();

//        if(beginPlace.startsWith("P")) {
//            Point loc = (Point) new WKTReader().read(beginPlace);
//            MapVertexInfo vertex = mapVertexInfoRepository.findByLocation(loc);
//            beginPlace = vertex.getVertexName();
//        }
//        if(arrivePlace.startsWith("P")) {
//            Point loc = (Point) new WKTReader().read(arrivePlace);
//            MapVertexInfo vertex = mapVertexInfoRepository.findByLocation(loc);
//            arrivePlace = vertex.getVertexName();
//        }
        String places = beginPlace + ' ' + arrivePlace;
        searHis.setSearchNames(places);
        searchHistoryRepository.save(searHis);
        return new ResponseEntity<>(new ErrorResponse(0,"Start Navigation!"), HttpStatus.OK);
    }
}
