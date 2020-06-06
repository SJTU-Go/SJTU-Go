package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Integer> {
    @Query(value = "SELECT * FROM notice " +
            "WHERE valid_begin_time < :localDateTime and valid_end_time > :localDateTime " +
            "ORDER BY valid_begin_time ASC", nativeQuery = true)
    List<Notice> findValidNotice(@Param("localDateTime") LocalDateTime localDateTime);
}
