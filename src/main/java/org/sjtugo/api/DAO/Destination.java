package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "destination")
public class Destination {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "placeid")
    private Integer placeID;

    @Column(name="place_name")
    private String placeName;

    @Column(name="nick_name")
    private String nickName;

    @Column(name="location")
    private Point location;

    @Column(name="place_info")
    private String placeInfo;
}
