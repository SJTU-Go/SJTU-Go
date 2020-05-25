package org.sjtugo.api.service.NavigateService;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.sjtugo.api.DAO.Entity.BusStop;

@Data
public class NavigatePlace {
    private String placeName;
    private Point location;
    private PlaceType placeType;
    private Integer placeID = 0;

    public NavigatePlace(){}

    public NavigatePlace(BusStop busStop){
        this.placeName = busStop.getStopName() +
                (busStop.getStopId() > 0 ?
                        "（逆时针方向）":"（顺时针方向）");
        this.location = busStop.getLocationPlatform();
        this.placeType = PlaceType.busStop;
        this.placeID = busStop.getStopId();
    }

    @Override
    public String toString(){
        return placeName + location.toString() + placeType.toString() + placeID;
    }

    enum PlaceType {
        destination,
        parking,
        point,
        busStop
    }
}
