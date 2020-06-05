package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    @Query(value = "SELECT * FROM notice " +
            "WHERE validBeginTime < localDateTime and validEndTime > localDateTime " +
            "ORDER BY validBeginTime ASC", nativeQuery = true)
    List<Notice> findValidNotice(LocalDateTime localDateTime);
}
