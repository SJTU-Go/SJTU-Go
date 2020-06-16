package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination,Integer> {
    List<Destination> findByPlaceNameLike(String kw);

    @Query( value = "SELECT * " +
            "FROM (SELECT ST_LENGTH(LineString(location, POINT(:lng, :lat))) AS distance, destination.*" +
            " FROM destination WHERE " +
            " lng < :lng+0.005 AND lng > :lng - 0.005 " +
            " AND lat < :lat+0.005 AND lat > :lat - 0.005) vertex_infos " +
            " ORDER BY vertex_infos.distance LIMIT 30",
            nativeQuery = true)
    List<Destination> findNearbyPoint(@Param("lng") Double lng, @Param("lat") Double lat);
}
