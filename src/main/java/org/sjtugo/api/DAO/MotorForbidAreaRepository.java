package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.MotorForbidArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MotorForbidAreaRepository extends JpaRepository<MotorForbidArea, Integer> {
    @Query(value = "SELECT COUNT(*)" +
            "FROM motor_forbid_area " +
            "WHERE ST_Contains(shape,ST_POLYGONFROMTEXT(CONCAT('Point(',:lng,' ',:lat,')'))) > 0" +
            "LIMIT 1",
            nativeQuery = true)
    Integer isFobidArea(@Param("lng") Double lng, @Param("lat") Double lat);
}
