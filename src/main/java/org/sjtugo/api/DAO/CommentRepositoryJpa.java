package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.sjtugo.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Integer> {

    //@Query(value = "select c from Comment  where intersects(location, GeomFromText(square))")
    List<Comment> findByLocationWithin(Polygon square);
    List<Comment> findByRelatedPlaceContains(Integer PlaceID);
}
