package org.sjtugo.api.DAO;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "bus_time_weekday")
public class BusTimeWeekday {
    @Id
    private Integer busid;

    @Column(name = "bus_time")
    private LocalTime busTime;
}
