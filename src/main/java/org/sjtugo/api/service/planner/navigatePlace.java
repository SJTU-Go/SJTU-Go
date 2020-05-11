package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

@Data
public class navigatePlace {
    private String placeName;
    private Point location;
    private PlaceType placeType;

    public navigatePlace(){}

    @Override
    public String toString(){
        return placeName + location.toString() + placeType.toString();
    }

    enum PlaceType {
        destination,
        parking,
        point
    }
}
