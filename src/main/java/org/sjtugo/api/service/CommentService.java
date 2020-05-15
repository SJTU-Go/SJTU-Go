package org.sjtugo.api.service;

import com.vividsolutions.jts.geom.Point;
import org.sjtugo.api.DAO.CommentRepository;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    public List<Comment> getCommentList(Point location){
        //Point currentLocation = location;
        return commentRepositoryJpa.findByLocation(location);
        }

    public List<Comment> getCommentList(Integer placeID){
        //List<Comment> comments = new ArrayList<Comment>();  //返回结果
        return commentRepositoryJpa.findAllByRelatedPlaceContains(placeID);
    }

    public Comment addComment(Comment commentInfo) { //要不要写这么多？？？
        Comment newComment = new Comment();
        newComment.setApproveUsers(commentInfo.getApproveUsers());
        newComment.setCommentID(commentInfo.getCommentID());
        newComment.setCommentTime(commentInfo.getCommentTime());
        newComment.setContents(commentInfo.getContents());
        //newComment.setLocation(commentInfo.getLocation());
        newComment.setRelatedPlace(commentInfo.getRelatedPlace());
        newComment.setSubComment(commentInfo.getSubComment());
        newComment.setTitle(commentInfo.getTitle());
        newComment.setUserID(commentInfo.getUserID());
        commentRepository.save(newComment); //直接save(commentInfo)???
        return newComment;
    }

    public void likeComment(Integer userID, Integer commentID) {
         Comment likedComment = commentRepository.findById(commentID).get();  //要用get返回实体对象
         List<Integer> approveUsers = likedComment.getApproveUsers();
         approveUsers.add(userID);
         likedComment.setApproveUsers(approveUsers); //不能写在一起？？
         commentRepository.save(likedComment);
         //return likedComment;
    }
}