package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Data
@Entity
@Table(name = "bus_stop")
public class BusStop {
    @Id
    @Column(name = "stopid")
    private Integer stopId;

    @Column(name = "location_platform")
    private Point locationPlatform;

    @Column(name = "location_stop")
    private Point locationStop;

    @Column(name = "stop_name")
    private String stopName;

    @Column(name = "next_route")
    private LineString nextRoute;

    @Column(name = "diff")
    private Long diff;

    public BusStop() {
    }

    public Duration getDiff(){
        return Duration.ofMinutes(diff);
    }

    public void setDiff(Duration t){
        this.diff = t.getSeconds()/60;
    }
}
