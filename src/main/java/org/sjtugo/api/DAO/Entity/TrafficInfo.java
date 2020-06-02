package org.sjtugo.api.DAO.Entity;


import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalTime;

@Data
@Entity
public class TrafficInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer trafficID;

    private LocalTime beginTime;

    private LocalTime endTime;

    private Duration repeat;

    private String message;

    private double motorSpeed;

    private double bikeSpeed;

    private double carSpeed;

    private Integer uploader;

}
