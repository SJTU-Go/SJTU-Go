package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.*;

@Data
@EqualsAndHashCode(callSuper=true)
public class BusRoute extends Route {
    private String departID;

    private String arriveID;

    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    private LocalTime departTime;

    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    private LocalTime arriveTime;
}
