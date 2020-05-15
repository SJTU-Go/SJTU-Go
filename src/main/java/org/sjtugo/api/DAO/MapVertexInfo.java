package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.*;
import java.awt.*;

@Data
@Entity
@Table(name = "map_vertex_info")
public class MapVertexInfo {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="vertexid")
    private Integer vertexID;

    @Column(name = "vertex_name")
    private String vertexName;

    @Column(name = "location")
    private Point location;

    @Column(name="bike_count")
    private int bikeCount;

    @Column(name="motor_count")
    private int motorCount;

    @Column(name="park_info")
    private String parkInfo;

    @Column(name="park_size")
    private String parkSize;
}
