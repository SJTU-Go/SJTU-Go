package org.sjtugo.api.controller;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import io.swagger.annotations.*;

import lombok.Data;
import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.controller.ResponseEntity.CommentResponse;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value="Comment System")
@RestController
@RequestMapping("/comments")
public class CommentControl {
    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;
    @Autowired
    private MapVertexInfoRepository mapVertexInfoRepository;
    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "get comments by place ID", notes = "给定地点ID，返回该处用户的评论，地点ID不存在则返回空列表")
    @GetMapping("/getcomments/place={placeID}")
    public @ResponseBody List<CommentResponse> getCommentByID(@PathVariable("placeID") Integer placeID) {
        CommentService commentser = new CommentService(commentRepositoryJpa, mapVertexInfoRepository, userRepository);
        Point loc;
        try{
            loc = mapVertexInfoRepository.findById(placeID).orElseThrow().getLocation();
        } catch (Exception e)  {
            return new ArrayList<>();
        }
        return commentser.getCommentList(loc,0);
    }

    @ApiOperation(value = "get comments by place location", notes = "给定地点经纬度,格式POINT(x y)，返回附近用户的评论")
    @PostMapping("/getcomments/loc")
    public @ResponseBody
    List<CommentResponse> getCommentList(@RequestParam String location) throws ParseException {
        CommentService commentser = new CommentService(commentRepositoryJpa, mapVertexInfoRepository, userRepository);
        Point loc;
        loc = (Point) new WKTReader().read(location);
        return commentser.getCommentList(loc,0);
    }

    @ApiOperation(value = "点击查看子评论", notes = "可能返回子评论中有空评论，不影响（评论8的一条子评论被删除）")
    @PostMapping("/subcomment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Comment.class),
            @ApiResponse(code = 404, message = "[2]Comment Not Found\n[3]FatherID Not Found", response = ErrorResponse.class)
    })
    public @ResponseBody ResponseEntity<?> getSubCommentList(@RequestParam Integer fatherID) {
        CommentService commentser = new CommentService(commentRepositoryJpa, mapVertexInfoRepository, userRepository);
        return commentser.getSubCommentList(fatherID);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping(value = "/addcomment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Comment.class),
            @ApiResponse(code = 404, message = "[2]Invalid Comment", response = ErrorResponse.class)
    })
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest){
        CommentService commentser = new CommentService(commentRepositoryJpa, mapVertexInfoRepository, userRepository);
        return commentser.addComment(
                commentRequest.getContents(),
                commentRequest.getUserID(),
                commentRequest.getRelatedPlace(),
                commentRequest.getFatherID());
    }

    @ApiOperation(value = "用户点赞功能")
    @PostMapping("/like")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "[2]Comment Not Found", response = ErrorResponse.class)
    })
    public ResponseEntity<ErrorResponse> likeComment(@RequestParam Integer userID, @RequestParam Integer commentID){
        CommentService commentser = new CommentService(commentRepositoryJpa, mapVertexInfoRepository, userRepository);
        return commentser.likeComment(userID, commentID);
    }

     @ApiOperation(value = "用户删除评论")
     @GetMapping("/delete={ID}")
     @ApiResponses(value = {
             @ApiResponse(code = 200, message = "OK", response = Comment.class),
             @ApiResponse(code = 404, message = "[2]Comment Not Found", response = ErrorResponse.class)
     })
     public ResponseEntity<ErrorResponse> deleteComment(@PathVariable("ID") Integer commentID){
        CommentService commentser = new CommentService(commentRepositoryJpa, mapVertexInfoRepository, userRepository);
        return commentser.deleteComment(commentID);
    }

    @Data
    static class CommentRequest {
        @ApiModelProperty(value = "评论内容")
        private String contents;
        @ApiModelProperty(value = "用户ID", example = "3")
        private Integer userID;
        @ApiModelProperty(value = "评论相关停车点ID", example = "134234")
        private Integer relatedPlace;   //前端传
        @ApiModelProperty(value = "父评论ID，若填0表示新评论", example = "34")
        private Integer fatherID;
    }
}