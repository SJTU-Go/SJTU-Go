package org.sjtugo.api.controller;


import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;

import org.sjtugo.api.DAO.CommentRepository;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.service.CommentService;
import org.sjtugo.api.service.planner.AddCommentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="Comment System")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/comments")
public class CommentControl {

    @ApiOperation(value = "get comment list by place ID", notes = "给定地点ID，返回该处用户的评论")
    @GetMapping("/place={placeID}")
    public @ResponseBody List<Comment> getCommentList(@PathVariable Integer placeID) {
        CommentService commentser = new CommentService();
        return commentser.getCommentList(placeID);
    }

    @ApiOperation(value = "get comment list by place location", notes = "给定地点经纬度，返回该处用户的评论")
    @PostMapping("/loc")
    public @ResponseBody List<Comment> getCommentList(@RequestParam Point location) {
            CommentService commentser = new CommentService();
        return commentser.getCommentList(location);
    }

    @PostMapping("/addcomment")
    @ExceptionHandler(AddCommentException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)  //
    public Comment addComment(@RequestBody Comment commentInfo) {
        CommentService commentser = new CommentService();
        return commentser.addComment(commentInfo);
    }

    @ApiOperation(value = "用户点赞功能")
    @PostMapping("/like")
    public void likeComment(@RequestParam Integer userID, @RequestParam Integer commentID){
        CommentService commentser = new CommentService();
        commentser.likeComment(userID, commentID);
    }

}