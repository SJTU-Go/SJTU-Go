package org.sjtugo.api.service.NavigateService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class NearBusResponse {

    private List<Integer> distances;

    private Integer status;

    private String message;

    private Integer nearestBus;

    private Integer distance;

    @SuppressWarnings("unchecked")
    @JsonProperty("result")
    private void unpackNested(Map<String,Object> result) {
        Map<String,Object> column = ((List<Map<String,Object>>) result.get("rows")).get(0);
//        System.out.println(column);
//        System.out.println(column.get("elements").getClass());
        List<Map<String,Object>> row = (List<Map<String,Object>>) column.get("elements");
        this.distances =  row.stream()
                     .map(rowElem -> (Integer) rowElem.get("distance"))
                     .collect(Collectors.toList());
        this.distance = Collections.min(this.distances);
        this.nearestBus = this.distances.indexOf(this.distance);
    }
}
