package org.sjtugo.api.controller;

import com.vividsolutions.jts.io.ParseException;
import io.swagger.annotations.*;

import lombok.Data;
import net.sf.json.JSONObject;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.service.Exception.AddCommentException;
import org.sjtugo.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value="Comment System")
@RestController
@RequestMapping("/comments")
public class CommentControl {
    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @ApiOperation(value = "get comments by place ID", notes = "给定地点ID，返回该处用户的评论")
    @GetMapping("/getcomments/place={placeID}")
    public @ResponseBody List<Comment> getCommentByID(@PathVariable("placeID") Integer placeID) {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.getCommentList(placeID);
    }

    @ApiOperation(value = "get comments by place location", notes = "给定地点经纬度,格式POINT(x y)，返回附近用户的评论")
    @PostMapping("/getcomments/loc")
    public @ResponseBody
    List<Comment> getCommentList(@RequestParam String location) throws ParseException {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        return commentser.getCommentList(location);
    }

    @ApiOperation(value = "点击查看子评论", notes = "可能返回子评论中有空评论（评论8的一条子评论被删除）")
    @PostMapping("/subcomment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Comment.class),
            @ApiResponse(code = 500, message = "[2]Comment Not Found", response = ErrorResponse.class)
    })
    public @ResponseBody ResponseEntity<?> getSubCommentList(@RequestParam Integer fatherID) {
        CommentService commentser = new CommentService(commentRepositoryJpa);
        try {
            return commentser.getSubCommentList(fatherID);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(500,"No such comment!"),HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "添加评论")
    @PostMapping(value = "/addcomment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Comment.class),
            @ApiResponse(code = 500, message = "[2]Invalid Comment", response = ErrorResponse.class)
    })
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest){
        CommentService commentser = new CommentService(commentRepositoryJpa);
        try {
            return commentser.addComment(commentRequest.getTitle(),
                    commentRequest.getContents(),
                    commentRequest.getUserID(),
                    commentRequest.getLocation(),
                    commentRequest.getRelatedPlace(),
                    commentRequest.getName(),
                    commentRequest.getFatherID());
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(500,"Invalid comment!"),HttpStatus.BAD_REQUEST);
        }

    }

    @ApiOperation(value = "用户点赞功能")
    @PostMapping("/like")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "[2]Comment Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<ErrorResponse> likeComment(@RequestParam Integer userID, @RequestParam Integer commentID){
        CommentService commentser = new CommentService(commentRepositoryJpa);
        try {
            return commentser.likeComment(userID, commentID);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(500,"No such comment!"), HttpStatus.BAD_REQUEST);
        }
    }

     @ApiOperation(value = "用户删除评论")
     @GetMapping("/delete={ID}")
     @ApiResponses(value = {
             @ApiResponse(code = 200, message = "OK", response = Comment.class),
             @ApiResponse(code = 500, message = "[2]Comment Not Found", response = ErrorResponse.class)
     })
     public ResponseEntity<ErrorResponse> deleteComment(@PathVariable("ID") Integer commentID){
        CommentService commentser = new CommentService(commentRepositoryJpa);
        try {
            return commentser.deleteComment(commentID);
        }catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(500,"No such comment!"), HttpStatus.BAD_REQUEST);
        }
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
        @ApiModelProperty(value = "评论相关停车点名")
        private String name;
        @ApiModelProperty(value = "父评论ID，若填0表示新评论", example = "34")
        private Integer fatherID;
    }
}