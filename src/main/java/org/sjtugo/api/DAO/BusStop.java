package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Data
@Entity
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stopIdGenerator")
//    @GenericGenerator(name = "stopIdGenerator",
//            strategy = "org.sjtugo.api.DAO.stopIdGenerator")
    private Integer stopId;

    private boolean isClock;

    private int stopNo;

    private LocalTime nextBus;

    private Point location;

    private String stopName;

    private LineString nextRoute;

    public BusStop() {

    }

    public BusStop(boolean isClock, int stopNo,
                   String nextBus, String location, String stopName,
                   String nextRoute){
        this.isClock = isClock;
        try {
            this.location = (Point) new WKTReader().read(location); //TODO:conversion
            this.nextRoute = (LineString) new WKTReader().read(nextRoute);
        } catch (ParseException e) {
            throw new IdSyntaxException("busID"); //TODO:error!
        }
        this.stopNo = stopNo;
        try {
            this.nextBus = LocalTime.parse(nextBus);
        } catch (DateTimeParseException e) {
            throw new IdSyntaxException("busID"); //TODO:error!
        }
        this.stopName = stopName;
    }

    public BusStop(Integer stopId, boolean isClock, int stopNo,
                   String nextBus, String location, String stopName,
                   String nextRoute){
        this.isClock = isClock;
        try {
            this.location = (Point) new WKTReader().read(location); //TODO:conversion
            this.nextRoute = (LineString) new WKTReader().read(nextRoute);
        } catch (ParseException e) {
            throw new IdSyntaxException("busID"); //TODO:error!
        }
        this.stopId = stopId;
        this.stopNo = stopNo;
        try {
            this.nextBus = LocalTime.parse(nextBus);
        } catch (DateTimeParseException e) {
            throw new IdSyntaxException("busID"); //TODO:error!
        }
        this.stopName = stopName;
    }
}
