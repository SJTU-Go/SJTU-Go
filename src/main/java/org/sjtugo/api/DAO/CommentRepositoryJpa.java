package org.sjtugo.api.DAO;

import com.vividsolutions.jts.geom.Point;
import org.sjtugo.api.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Integer> { //
    List<Comment> findByLocation(Point location);
    List<Comment> findAllByRelatedPlaceContains(Integer PlaceID);
}
