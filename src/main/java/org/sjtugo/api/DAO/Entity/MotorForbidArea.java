package org.sjtugo.api.DAO.Entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.geolatte.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "motor_forbid_area")
public class MotorForbidArea {
    @Id
    @Column(name="areaID")
    @ApiModelProperty(value = "禁停点id", example = "23")
    private Integer areaID;

    @Column(name = "shape")
    @ApiModelProperty(value = "禁停区域", example = "geometry polygon类型")
    private Geometry shape;

}
