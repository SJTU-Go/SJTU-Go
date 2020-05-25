package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Polygon;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapVertexInfoRepository extends JpaRepository<MapVertexInfo, Integer> {
    List<MapVertexInfo> findByVertexNameLike(String kw);

    @Query( value = "SELECT * FROM map_vertex_info WHERE vertex_name IS NOT NULL AND (MBRWithin(location,:window)) = TRUE",
            nativeQuery = true)
    List<MapVertexInfo> findNearbyPoint(@Param("window") Polygon windows);

    @Query( value = "SELECT vertex_infos.*" +
            "  FROM (" +
            "     SELECT ST_LENGTH(LineString(location,POINT(:lng,:lat))) AS distance," +
            "            location, vertexid, bike_count, motor_count, park_info, park_size, vertex_name" +
            "       FROM map_vertex_info" +
            "      WHERE  MBRContains(" +
            "                GeomFromText(" +
            "                       CONCAT('LINESTRING('," +
            "                               :lng - 0.1,' ', :lat - 0.1, ','," +
            "                               :lng + 0.1,' ', :lat + 0.1, ')'))," +
            "                location ) AND vertex_name IS NOT NULL" +
            "       ) AS vertex_infos " +
            "ORDER BY vertex_infos.distance " +
            "LIMIT 1", nativeQuery = true)
    List<MapVertexInfo> findNearbyParking(@Param("lng") double lng, @Param("lat") double lat);

}
