package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@ApiModel("修改记录")
public class Modification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "记录ID",example = "1")
    private Integer modificationID;

    @ApiModelProperty(value = "修改者ID(管理员)", example = "1")
    private Integer adminID;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private LocalDateTime time;

    @ApiModelProperty(value = "具体修改信息")
    private String contents;

    public Modification() {}
}
