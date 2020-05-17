package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.sjtugo.api.DAO.CommentRepository;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public class CommentService {
    private CommentRepositoryJpa commentRepositoryJpa;

    public CommentService(CommentRepositoryJpa commentRepositoryJpa) {
        this.commentRepositoryJpa = commentRepositoryJpa;
    }

    public List<Comment> getCommentList(Point location){
        //Point currentLocation = location;
        return commentRepositoryJpa.findByLocation(location);
        }

    public List<Comment> getCommentList(Integer placeID){
        //List<Comment> comments = new ArrayList<Comment>();  //返回结果
        return commentRepositoryJpa.findAllByRelatedPlaceContains(placeID);
    }

    public Comment addComment(String contents, Integer userID) {
        Comment newComment = new Comment();
        newComment.setContents(contents);
        newComment.setUserID(userID);
        newComment.setCommentTime(LocalDateTime.now());
        commentRepositoryJpa.save(newComment);
        return newComment;
    }

    public Comment addComment(String title, String contents, Integer userID, String location) throws ParseException {
        Comment newComment = new Comment();
        newComment.setTitle(title);
        newComment.setContents(contents);
        newComment.setUserID(userID);
        newComment.setCommentTime(LocalDateTime.now());
        Point loc = (Point) new WKTReader().read(location);
        newComment.setLocation(loc);
        commentRepositoryJpa.save(newComment);
        return newComment;
    }

    public void likeComment(Integer userID, Integer commentID) {
         Comment likedComment = commentRepositoryJpa.findById(commentID).get();  //get返回实体对象 获取被点赞的评论
         List<Integer> approveUsers = likedComment.getApproveUsers();  //之前点赞的用户ID
         approveUsers.add(userID);  //添当前点赞用户ID
         likedComment.setApproveUsers(approveUsers);
         commentRepositoryJpa.save(likedComment);
         System.out.println("点赞+1");
    }
}