package org.sjtugo.api.controller;


import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.DAO.CommentRepository;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="Comment System")
@RestController
@RequestMapping("/comments")
public class CommentControl {
    @Autowired
    private CommentRepository commentRepository;

    @ApiOperation(value = "Comment Service", notes = "给定地点ID，返回该处用户的评论")
    @GetMapping("/place")
    public @ResponseBody List<Comment> getCommentList(@RequestParam Integer placeID) {
        CommentService commentser = new CommentService();
        return commentser.getCommentList(placeID);
    }

    @ApiOperation(value = "Comment Service", notes = "给定地点经纬度，返回该处用户的评论")
    @PostMapping("/loc")
    public @ResponseBody List<Comment> getCommentList(@RequestParam Point location) {
            CommentService commentser = new CommentService();
        return commentser.getCommentList(location);
    }

    @PostMapping("/addcomment")
    public int addComment(@RequestBody Comment commentInfo) {
        //CommentService commentser = new CommentService();
        //return commentser.addComment(commentInfo.getCommentInfo());
        Comment newComment = new Comment();
        newComment.setApproveUsers(commentInfo.getApproveUsers());
        newComment.setCommentID(commentInfo.getCommentID());
        newComment.setCommentTime(commentInfo.getCommentTime());
        newComment.setContents(commentInfo.getContents());
        newComment.setLocation(commentInfo.getLocation());
        newComment.setRelatedPlace(commentInfo.getRelatedPlace());
        newComment.setSubComment(commentInfo.getSubComment());
        newComment.setTitle(commentInfo.getTitle());
        newComment.setUserID(commentInfo.getUserID());
        commentRepository.save(newComment);
        return (int) (commentRepository.count()); //+1?
    }

}