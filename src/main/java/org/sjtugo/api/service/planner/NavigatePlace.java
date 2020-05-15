package org.sjtugo.api.service.planner;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.sjtugo.api.DAO.BusStop;

@Data
public class NavigatePlace {
    private String placeName;
    private Point location;
    private PlaceType placeType;

    public NavigatePlace(){}

    public NavigatePlace(BusStop busStop){
        this.placeName = busStop.getStopName() +
                (busStop.getStopId() > 0 ?
                        "（逆时针方向）":"（顺时针方向）");
        this.location = busStop.getLocationPlatform();
        this.placeType = PlaceType.busStop;
    }

    @Override
    public String toString(){
        return placeName + location.toString() + placeType.toString();
    }

    enum PlaceType {
        destination,
        parking,
        point,
        busStop
    }
}
