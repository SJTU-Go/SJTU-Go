package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.JindouyunInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JindouyunRepository extends JpaRepository<JindouyunInfo,Integer> {
    /*@Query( value = "SELECT COUNT(*) FROM jindouyun_info WHERE TIMESTAMPDIFF(SECOND ,time,CURRENT_TIMESTAMP) < 300" +
            " AND cluster_point = :id",
            nativeQuery = true)
    int findBikeCounts(@Param("id") String id);*/

    /*@Query( value = "SELECT *, ST_LENGTH(LineString(POINT(longitude,latitude),POINT(:longi,:lati))) AS distance" +
            " FROM jindouyun_info WHERE " +
            "longitude < 0.002 + :longi AND :longi - 0.002 < longitude " +
            "AND latitude  < :lati + 0.002 AND latitude > :lati - 0.002 " +
            "AND ABS(latitude - :lati)< 0.002 AND TIMESTAMPDIFF(SECOND,time,current_timestamp) < 300" +
            " ORDER BY distance LIMIT 30",
            nativeQuery = true)
    List<JindouyunInfo> findNearbyBikes(@Param("longi") double longi, @Param("lati") double lati); //, @Param("time")LocalDateTime time);*/

    //without time limit
    @Query( value = "SELECT COUNT(*) FROM jindouyun_info WHERE TIMESTAMPDIFF(SECOND ,time,CURRENT_TIMESTAMP) < 300" +
            " AND cluster_point = :id",
            nativeQuery = true)
    int findBikeCounts(@Param("id") String id);

    @Query( value = "SELECT *, ST_LENGTH(LineString(POINT(longitude,latitude),POINT(:longi,:lati))) AS distance" +
            " FROM jindouyun_info WHERE " +
            "longitude < 0.002 + :longi AND :longi - 0.002 < longitude " +
            "AND latitude  < :lati + 0.002 AND latitude > :lati - 0.002 " +
            "AND ABS(latitude - :lati)< 0.002" +
            " ORDER BY distance LIMIT 30",
            nativeQuery = true)
    List<JindouyunInfo> findNearbyBikes(@Param("longi") double longi, @Param("lati") double lati); //, @Param("time")LocalDateTime time);

    List<JindouyunInfo> findAllByLngBetweenAndLatBetweenAndTimeAfter(double lng1,double lng2,double lat1,double lat2, LocalDateTime time);


//    @Query( value = "SELECT u FROM HelloBikeInfo u")
//    List<HelloBikeInfo> findNearbyBikes(); //, @Param("time")LocalDateTime time);


}

