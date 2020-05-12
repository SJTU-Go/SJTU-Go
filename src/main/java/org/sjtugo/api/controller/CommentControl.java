package org.sjtugo.api.controller;


import com.vividsolutions.jts.geom.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value="Comment System")
@RestController
@RequestMapping("/comments")
public class CommentControl {

    @ApiOperation(value = "Comment Service", notes = "给定地点经纬度，返回该处用户的评论")
    @PostMapping("/comment")
    public List<Comment> getCommentList(@RequestParam Integer placeID) {
        CommentService commentser = new CommentService();
        return commentser.getCommentList(placeID);
    }

    @PostMapping("comments/addcomment")
    public int addComment(@RequestBody CommentRequest commentInfo) {
        CommentService commentser = new CommentService();
        return commentser.addComment(commentInfo.getCommentInfo());
    }

    @ApiModel(value = "添加评论输入数据")
    @Data
    static class CommentRequest {
        @ApiModelProperty(value = "")
        private Comment commentInfo;
    }
}