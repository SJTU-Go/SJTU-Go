package org.sjtugo.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.NoticeRepository;
import org.sjtugo.api.entity.Notice;
import org.sjtugo.api.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(value = "Notice System")
@RestController
@RequestMapping("/notice")
public class NoticeControl {
    @Autowired
    private NoticeRepository noticeRepository;

    @ApiOperation(value = "发布公告")
    @PostMapping("/post")
    public ResponseEntity<?> postNotice(@RequestBody NoticeRequest noticeRequest){
        NoticeService noticeService = new NoticeService(noticeRepository);
        return noticeService.postNotice(noticeRequest.getPublisherID(),
                noticeRequest.getContents(),
                noticeRequest.getValidBeginTime(),
                noticeRequest.getValidEndTime());
    }

    @ApiOperation(value = "获取公告")
    @PostMapping("/request")
    public @ResponseBody List<Notice> requestNotice(){
        NoticeService noticeService = new NoticeService(noticeRepository);
        return noticeService.requestNotice(LocalDateTime.now());
    }

    @Data
    static class NoticeRequest{
        @ApiModelProperty(value = "发布者ID", example = "123")
        private Integer publisherID;

        @ApiModelProperty(value = "公告内容")
        private String contents;

        @ApiModelProperty(value = "公告起始时间", example = "2020/06/02 12:00")
        @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
        private LocalDateTime validBeginTime;

        @ApiModelProperty(value = "公告结束时间", example = "2020/07/02 12:00")
        @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm", timezone = "GMT+8")
        private LocalDateTime validEndTime;
    }
}
