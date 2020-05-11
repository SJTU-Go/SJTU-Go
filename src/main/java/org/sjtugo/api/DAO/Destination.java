package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@Entity
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer placeID;

    private String placeName;

    private String nickName;

    private Point location;

    private String placeInfo;
}
