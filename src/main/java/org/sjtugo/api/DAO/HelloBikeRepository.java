package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Polygon;
import org.sjtugo.api.entity.HelloBikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

public interface HelloBikeRepository extends JpaRepository<HelloBikeInfo,Integer> {
    @Query( value = "SELECT COUNT(*) FROM helloBikeInfo WHERE TIMESTAMPDIFF(SECOND , CURRENT_TIMESTAMP, time) < 120" +
            " AND cluster_point = :id",
            nativeQuery = true)
    int findBikeCounts(@Param("id") String id);

//    @Query( value = "SELECT u FROM HelloBikeInfo u WHERE ABS(u.lng - :longi) < 0.002 " +
//            "AND ABS(u.lat - :lati)< 0.002")
//    List<HelloBikeInfo> findNearbyBikes(@Param("longi") double longi, @Param("lati") double lati); //, @Param("time")LocalDateTime time);

    List<HelloBikeInfo> findAllByLngBetweenAndLatBetweenAndTimeAfter(double lng1,double lng2,double lat1,double lat2, LocalDateTime time);


//    @Query( value = "SELECT u FROM HelloBikeInfo u")
//    List<HelloBikeInfo> findNearbyBikes(); //, @Param("time")LocalDateTime time);


}
