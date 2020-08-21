package org.sjtugo.api.entity;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.service.NavigateService.NavigatePlace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransitionRoute extends Route {
    private String placeID;

    private String parkID;

    private boolean isDepart;

    private List<TransitionRoute> optionals;

    public TransitionRoute(boolean isDepart){
        this.isDepart = isDepart;
        this.type = isDepart ? RouteType.FIND : RouteType.PARK;
        this.distance = 100; // TODO default value
        this.optionals = new ArrayList<>();
    }

    public TransitionRoute(boolean isDepart, NavigatePlace start, List<MapVertexInfo> pickups){
        this.isDepart = isDepart;
        this.type = isDepart ? RouteType.FIND : RouteType.PARK;
        MapVertexInfo pickup = pickups.get(0);
        this.setParkID(String.valueOf(pickup.getVertexID()));
        this.setPlaceID(start.getPlaceType().name() + start.getPlaceID());
        if (isDepart) {
            this.setDepartName(start.getPlaceName());
            this.setArriveName(pickup.getVertexName() + "（寻车点）");
        } else {
            this.setDepartName(pickup.getVertexName() + "（停车点）");
            this.setArriveName(start.getPlaceName());
        }

        LineString path = new GeometryFactory().createLineString(
                new Coordinate[]{
                        start.getLocation().getCoordinate(),
                        pickup.getLocation().getCoordinate()}
        );
        this.setRoutePath(path);
        this.setDistance((int) (path.getLength()*111195 ));
        this.setRouteTime((int) (path.getLength()*111195 /1.25));
        if (isDepart){
            this.setOptionals(pickups.subList(1,pickups.size()-1).stream().map(
                    elemPickup -> {
                        TransitionRoute startTransition = new TransitionRoute(true);
                        startTransition.setParkID(String.valueOf(elemPickup.getVertexID()));
                        startTransition.setPlaceID(start.getPlaceType().name() + start.getPlaceID());
                        startTransition.setDepartName(start.getPlaceName());
                        startTransition.setArriveName(elemPickup.getVertexName() +  "（寻车点）");
                        LineString elemPath = new GeometryFactory().createLineString(
                                new Coordinate[]{
                                        start.getLocation().getCoordinate(),
                                        elemPickup.getLocation().getCoordinate()}
                        );
                        startTransition.setRoutePath(elemPath);
                        startTransition.setDistance((int) (elemPath.getLength() * 111195));
                        startTransition.setRouteTime((int) (elemPath.getLength() * 111195 / 1.25));
                        return startTransition;
                    }).collect(Collectors.toList()));
        }
    }

}
