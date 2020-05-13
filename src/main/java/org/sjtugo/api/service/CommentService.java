package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Point;
import org.sjtugo.api.entity.Comment;

import java.util.List;

public class CommentService {

    public List<Comment> getCommentList(Point location){
        Point currentLocation = location;
        return null;}

    public List<Comment> getCommentList(Integer placeID){
        List<Comment> comments;

        return null;}

    //public List<Comment> getCommentList(String parkingID){ return null;}

    public int addComment(Comment commentInfo) {
        int number = commentInfo.getCommentID();
        return number;
    }

    public int likeComment(String userID, String commentID) {return 0;}
}