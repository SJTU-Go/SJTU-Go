package org.sjtugo.api.service;

import org.sjtugo.api.DAO.NoticeRepository;
import org.sjtugo.api.entity.Notice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeService {
    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository){
        this.noticeRepository = noticeRepository;
    }

    public ResponseEntity<?> postNotice(Integer publisherID, String contents,
                                       LocalDateTime validBeginTime, LocalDateTime validEndTime){
        Notice newNotice = new Notice();
        newNotice.setPublisherID(publisherID);
        newNotice.setContents(contents);
        newNotice.setValidBeginTime(validBeginTime);
        newNotice.setValidEndTime(validEndTime);
        newNotice.setPublishTime(LocalDateTime.now());
        noticeRepository.save(newNotice);
        return new ResponseEntity<>(newNotice, HttpStatus.OK);
    }

    public List<Notice> requestNotice(LocalDateTime localDateTime){
        return noticeRepository.findValidNotice(localDateTime);
    }

}
