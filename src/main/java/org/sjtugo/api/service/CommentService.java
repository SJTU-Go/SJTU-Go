package org.sjtugo.api.service;


import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentService {

    private final CommentRepositoryJpa commentRepositoryJpa;

    static double r = 2;

    public CommentService(CommentRepositoryJpa commentRepositoryJpa) {
        this.commentRepositoryJpa = commentRepositoryJpa;
    }

    /**
     *
     * @param location:地点，前端传入，格式POINT(x y)
     * @return (x-r y+r)~(x+r y-r)范围内的评论列表
     * @throws ParseException String转Point
     */
    public List<Comment> getCommentList(String location) throws ParseException {

        Point loc = (Point) new WKTReader().read(location);
        double x = loc.getX();
        double y = loc.getY();
        double x1 = x-r;
        double x2 = x+r;
        double y1 = y-r;
        double y2 = y+r;
        String polygon = "POLYGON(("
                + x1 + " " + y1 + "," + x2 + " " + y1 + "," + x1 + " " + y2 + "," + x2 + " " + y2 + "," + x1 + " " + y1
                +"))";
        Polygon square = (Polygon) new WKTReader().read(polygon);
        return commentRepositoryJpa.findByLocationWithin(square);
    }

    public List<Comment> getCommentList(Integer placeID){
        return commentRepositoryJpa.findByRelatedPlace(placeID);
    }

    /**
     * 添加评论/回复评论
     * @param title 标题
     * @param contents 内容
     * @param userID 评论者ID
     * @param location 评论地点
     * @param relatedPlace 评论相关位置ID
     * @param name 停车点名
     * @param fatherID 父评论ID
     * @return 本次添加的新评论
     */
    public ResponseEntity<?> addComment(String title, String contents, Integer userID,
                                             String location, Integer relatedPlace, String name,
                                             Integer fatherID) {
        Comment newComment = new Comment();
        newComment.setTitle(title);
        newComment.setContents(contents);
        newComment.setUserID(userID);
        newComment.setParkingName(name);
        newComment.setCommentTime(LocalDateTime.now());
        Point loc;
        try {
            loc = (Point) new WKTReader().read(location);
        } catch (ParseException e){
            return new ResponseEntity<>(new ErrorResponse
                    (e), HttpStatus.BAD_REQUEST);
        }
        newComment.setLocation(loc);
        newComment.setRelatedPlace(relatedPlace);
        commentRepositoryJpa.save(newComment);

        Comment fatherComment = commentRepositoryJpa.findById(fatherID).orElse(null);
        assert fatherComment != null;
        try {
            List<Integer> subComments = fatherComment.getSubComment();  //得到子评论
            subComments.add(newComment.getCommentID());
            fatherComment.setSubComment(subComments);
            commentRepositoryJpa.save(fatherComment);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(2,"Can reply!"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    /**
     * 点赞
     * @param userID 点赞者ID
     * @param commentID 被点赞评论ID
     */
    public ResponseEntity<ErrorResponse> likeComment(Integer userID, Integer commentID) {
         Comment likedComment = commentRepositoryJpa.findById(commentID).orElse(null);  //get返回实体对象 获取被点赞的评论
         assert likedComment != null;
         try{
             List<Integer> approveUsers = likedComment.getApproveUsers();  //之前点赞的用户ID
             approveUsers.add(userID);  //添当前点赞用户ID
             likedComment.setApproveUsers(approveUsers);
             commentRepositoryJpa.save(likedComment);
             return new ResponseEntity<>(new ErrorResponse(0,"点赞+1"),HttpStatus.OK);
         } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(2,"No such comment!"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 返回子评论
     * @param fatherID:父评论ID
     * @return 子评论列表
     */
    public ResponseEntity<?> getSubCommentList(Integer fatherID) {
        List<Comment> subComments = new ArrayList<>();
        Comment fatherComment = commentRepositoryJpa.findById(fatherID).orElse(null);
        assert fatherComment != null;
        try {
            List<Integer> subCommentsID = fatherComment.getSubComment();
            for (Integer subCommentID : subCommentsID)
            {
                Comment subComment = commentRepositoryJpa.findById(subCommentID).orElse(null);
                subComments.add(subComment);
            }
            return new ResponseEntity<>(subComments,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(2,"No such comment!"),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<ErrorResponse> deleteComment(Integer commentID) {
        Comment comment = commentRepositoryJpa.findById(commentID).orElse(null);
        assert comment != null;
        try {
            List<Integer> subCommentsID = comment.getSubComment();
            for (Integer ID : subCommentsID) {
                commentRepositoryJpa.deleteById(ID);
            }
            commentRepositoryJpa.deleteById(commentID);
            return new ResponseEntity<>(new ErrorResponse(0, "delete successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(2,"No such comment!"), HttpStatus.BAD_REQUEST);
        }
    }

}