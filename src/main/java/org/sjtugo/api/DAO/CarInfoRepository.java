package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Polygon;
import org.sjtugo.api.DAO.Entity.CarInfo;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarInfoRepository extends JpaRepository<CarInfo, Integer> {

    @Query( value = "SELECT vertex_infos.*" +
            "  FROM (" +
            "     SELECT ST_LENGTH(LineString(POINT(longitude,latitude),POINT(:lng,:lat))) AS distance," +
            "            e100_info.* " +
            "       FROM e100_info" +
            "      WHERE  MBRContains(" +
            "                GeomFromText(" +
            "                       CONCAT('LINESTRING('," +
            "                               :lng - 0.1,' ', :lat - 0.1, ','," +
            "                               :lng + 0.1,' ', :lat + 0.1, ')'))," +
            "                POINT(longitude,latitude) )" +
            "       ) AS vertex_infos " +
            "ORDER BY vertex_infos.distance " +
            "LIMIT 1", nativeQuery = true)
    List<CarInfo> findNearbyCar(@Param("lng") double lng, @Param("lat") double lat);

}
