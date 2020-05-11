package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;

@Data
@Entity
public class MapVertexInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer vertexID;

    private String vertexName;

    private Point location;

    private int bikeCount;

    private int motorCount;

    private String parkInfo;

    private String parkSize;
}
