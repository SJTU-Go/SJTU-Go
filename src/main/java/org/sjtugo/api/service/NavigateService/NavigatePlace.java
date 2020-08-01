package org.sjtugo.api.service.NavigateService;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.sjtugo.api.DAO.Entity.BusStop;
import org.sjtugo.api.DAO.Entity.CarInfo;
import org.sjtugo.api.DAO.Entity.JindouyunInfo;

@Data
public class NavigatePlace {
    private String placeName;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
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

    public NavigatePlace(CarInfo carInfo){
        this.placeName = carInfo.getCarPlate()+"所在位置";
        this.location = new GeometryFactory().createPoint(new Coordinate(carInfo.getLongitude(),carInfo.getLatitude()));
        this.placeType = PlaceType.car;
    }

    public NavigatePlace(JindouyunInfo jindouyunInfo){
        this.placeName =  jindouyunInfo.getBikeID().toString() + "所在位置";
        this.location = new GeometryFactory().createPoint(new Coordinate(jindouyunInfo.getLng(),jindouyunInfo.getLat()));
        this.placeType = PlaceType.car;
    }

    @Override
    public String toString(){
        return placeName + location.toString() + placeType.toString() + placeID;
    }

    enum PlaceType {
        destination,
        parking,
        point,
        busStop,
        car
    }
}
