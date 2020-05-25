package org.sjtugo.api.controller;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import lombok.Data;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.entity.ErrorResponse;
import org.sjtugo.api.service.AddCommentException;
import org.sjtugo.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody List<Comment> getCommentByID(@PathVariable("placeID") Integer placeID) {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.getCommentList(placeID);
    }

    @ApiOperation(value = "get comments by place location", notes = "给定地点经纬度，返回附近用户的评论")
    @PostMapping("/loc")
    public @ResponseBody List<Comment> getCommentList(@RequestParam String location) throws ParseException {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.getCommentList(location);
    }

    @ApiOperation(value = "点击查看子评论")
    @PostMapping("/subcomment")
    public @ResponseBody List<Comment> getSubCommentList(@RequestParam Integer fatherID) {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.getSubCommentList(fatherID);
    }

    @PostMapping(value = "/addcomment")
    @ExceptionHandler(AddCommentException.class)
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest){
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.addComment(commentRequest.getTitle(),
                commentRequest.getContents(),
                commentRequest.getUserID(),
                commentRequest.getLocation(),
                commentRequest.getRelatedPlace(),
                commentRequest.getFatherID());
    }

    @ApiOperation(value = "用户点赞功能")
    @PostMapping("/like")
    public ResponseEntity<ErrorResponse> likeComment(@RequestParam Integer userID, @RequestParam Integer commentID){
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.likeComment(userID, commentID);
    }

    @Data
    static class CommentRequest {
        @ApiModelProperty(value = "评论内容")
        private String contents;
        @ApiModelProperty(value = "评论名", example="评论一")
        private String title;
        @ApiModelProperty(value = "用户ID", example = "3")
        private Integer userID;
        @ApiModelProperty(value = "坐标位置",
                example = "POINT (121.437689 31.025735)")
        private String location;  //前端传
        @ApiModelProperty(value = "评论相关停车点ID", example = "134234")
        private Integer relatedPlace;   //前端传
        @ApiModelProperty(value = "父评论ID，若填0表示新评论", example = "34")
        private Integer fatherID; //=0表示是父评论
    }
}