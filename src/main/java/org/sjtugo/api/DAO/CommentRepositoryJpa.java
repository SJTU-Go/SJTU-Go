package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.sjtugo.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Integer> {


    @Query(value = "SELECT * FROM comment where mbrwithin(location,:window) AND father_comment = :father_id",
    nativeQuery = true)
    List<Comment> findByLocationWithin(@Param("window") Polygon square, @Param("father_id") Integer father_id);


    List<Comment> findByRelatedPlace(Integer PlaceID);
}
