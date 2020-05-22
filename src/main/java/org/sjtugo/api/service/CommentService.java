package org.sjtugo.api.service;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public class CommentService {

    private final CommentRepositoryJpa commentRepositoryJpa;
    private final GeometryFactory geometryFactory = new GeometryFactory();
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
        System.out.println(polygon);
        Polygon square = (Polygon) new WKTReader().read(polygon);
        return commentRepositoryJpa.findByLocationWithin(square);
    }

    public List<Comment> getCommentList(Integer placeID){
        return commentRepositoryJpa.findByRelatedPlaceContains(placeID);
    }

    /**
     * 添加评论/回复评论
     * @param title 标题
     * @param contents 内容
     * @param userID 评论者ID
     * @param location 评论地点
     * @param relatedPlace 评论相关位置ID
     * @param fatherID 父评论ID
     * @return 本次添加的新评论
     * @throws ParseException String类转Point
     */
    public Comment addComment(String title, String contents, Integer userID,
                              String location, List<Integer> relatedPlace,
                              Integer fatherID) throws ParseException {
        Comment newComment = new Comment();
        newComment.setTitle(title);
        newComment.setContents(contents);
        newComment.setUserID(userID);
        newComment.setCommentTime(LocalDateTime.now());
        Point loc = (Point) new WKTReader().read(location);
        newComment.setLocation(loc);
        newComment.setRelatedPlace(relatedPlace);
        commentRepositoryJpa.save(newComment);

        if (fatherID!=0) {
            Comment fatherComment = commentRepositoryJpa.findById(fatherID).orElse(null);  //找到父评论
            assert fatherComment != null;
            List<Integer> subComments = fatherComment.getSubComment();  //得到子评论
            subComments.add(newComment.getCommentID());
            fatherComment.setSubComment(subComments);
            commentRepositoryJpa.save(fatherComment);
        }
        return newComment;
    }

    /**
     * 点赞
     * @param userID 点赞者ID
     * @param commentID 被点赞评论ID
     */
    public void likeComment(Integer userID, Integer commentID) {
         Comment likedComment = commentRepositoryJpa.findById(commentID).orElse(null);  //get返回实体对象 获取被点赞的评论
         assert likedComment != null;
         List<Integer> approveUsers = likedComment.getApproveUsers();  //之前点赞的用户ID
         approveUsers.add(userID);  //添当前点赞用户ID
         likedComment.setApproveUsers(approveUsers);
         commentRepositoryJpa.save(likedComment);
         System.out.println("点赞+1");
    }
}