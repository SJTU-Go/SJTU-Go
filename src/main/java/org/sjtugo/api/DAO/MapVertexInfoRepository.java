package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapVertexInfoRepository extends JpaRepository<MapVertexInfo, Integer> {
    List<MapVertexInfo> findByVertexNameLike(String kw);

    @Query( value = "SELECT * FROM map_vertex_info WHERE vertex_name IS NOT NULL AND (MBRWithin(location,:window)) = TRUE",
            nativeQuery = true)
    List<MapVertexInfo> findNearbyPoint(@Param("window") Polygon windows);

}
