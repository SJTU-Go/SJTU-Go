package org.sjtugo.api.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@ApiModel
public class TimeStamp {
    @Id
    @ApiModelProperty(value = "时间戳",example = "11")
    private Integer stamp;

    @ApiModelProperty(value = "地图点", example = "123111")
    private Integer vertexid;

    public TimeStamp() {}
}
