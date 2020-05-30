package org.sjtugo.api.DAO.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "e100_info")
public class CarInfo {
    @Id
    @Column(name = "car_id")
    @ApiModelProperty(value = "旋风E100车辆ID")
    private String carID;

    @Column(name = "car_plate")
    private String carPlate;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name="time")
    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty(value = "爬取时间", example = "18:56:34")
    private LocalDateTime time;

    @Column(name="cluster_point")
    @ApiModelProperty(value = "最近路口ID，即导航时将贴到该路口搜寻路线，此处含义与哈罗单车不同", example = "175196")
    private String clusterPoint;
}
