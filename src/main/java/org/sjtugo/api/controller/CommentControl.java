package org.sjtugo.api.controller;


import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;

import lombok.Data;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.service.AddCommentException;
import org.sjtugo.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="Comment System")
@RestController  // the data returned by each method will be written straight into the response body
@RequestMapping("/comments")
public class CommentControl {
    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @ApiOperation(value = "get comments by place ID", notes = "给定地点ID，返回该处用户的评论")
    @GetMapping("/place={placeID}")
    public @ResponseBody List<Comment> getCommentList(@PathVariable Integer placeID) {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.getCommentList(placeID);
    }

    @ApiOperation(value = "get comments by place location", notes = "给定地点经纬度，返回该处用户的评论")
    @PostMapping("/loc")
    public @ResponseBody List<Comment> getCommentList(@RequestParam String loc) throws ParseException {
            CommentService commentser = new CommentService(commentRepositoryJpa);
            Point location = (Point) new WKTReader().read(loc); //记得处理location
        return commentser.getCommentList(location);
    }

    @PostMapping(value = "/addcomment1")  //没有title
    @ExceptionHandler(AddCommentException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public Comment addComment(@RequestParam String contents, @RequestParam Integer userID) {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.addComment(contents,userID);
    }
    @PostMapping(value = "/addcomment") //有title及location
    @ExceptionHandler(AddCommentException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public Comment addComment(@RequestBody CommentRequest commentRequest) throws ParseException {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.addComment(commentRequest.getTitle(),
                commentRequest.getContents(),
                commentRequest.getUserID(),
                commentRequest.getLocation());
    }

    @ApiOperation(value = "用户点赞功能")
    @PostMapping("/like")
    public void likeComment(@RequestParam Integer userID, @RequestParam Integer commentID){
        CommentService commentser = new CommentService(commentRepositoryJpa);
        commentser.likeComment(userID, commentID);
    }

    @Data
    static class CommentRequest {
        private String contents;
        private String title;
        private Integer userID;
        private String location;  //前端传
        //private List<Integer> relatedPlace;   //前端传？？？
    }
}