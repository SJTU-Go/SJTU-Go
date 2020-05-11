package org.sjtugo.api.service.planner;

import org.sjtugo.api.DAO.DestinationRepository;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.entity.Strategy;
import org.springframework.web.client.RestTemplate;

public class BusPlanner extends AbstractPlanner {

    public BusPlanner(MapVertexInfoRepository mapVertexInfoRepository,
                      DestinationRepository destinationRepository,
                      RestTemplate restTemplate){
        super(mapVertexInfoRepository,destinationRepository,restTemplate);
    }

    @Override
    public Strategy planOne(String beginPlace, String endPlace){
        return null;
    }
}
