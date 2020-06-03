package org.sjtugo.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@ApiModel(value = "公告")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "公告ID",example = "123")
    private Integer noticeID;

    @ApiModelProperty(value = "发布者ID", example = "123")
    private Integer publisherID;

    @ApiModelProperty(value = "公告内容")
    private String contents;

    @ApiModelProperty(value = "发布时间", example = "2020/06/02 12:00")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "公告起始时间", example = "2020/06/02 12:00")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime validBeginTime;

    @ApiModelProperty(value = "公告结束时间", example = "2020/07/02 12:00")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")  //传入的参数格式
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")  //输出参数格式化
    private LocalDateTime validEndTime;

    public Notice() {}
}
