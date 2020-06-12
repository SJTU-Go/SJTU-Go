package org.sjtugo.api.controller.ResponseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sjtugo.api.entity.Comment;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentResponse extends Comment {

    @ApiModelProperty(value = "评论者微信名", example = "小明")
    private String userName;

    @ApiModelProperty(value = "赞同者微信名", example = "[小明,小王]")
    private List<String> approveNames;


    public CommentResponse(Comment comment) {
        this.setSubComment(comment.getSubComment());
        this.setUserID(comment.getUserID());
        this.setApproveUsers(comment.getApproveUsers());
        this.setCommentTime(comment.getCommentTime());
        this.setFatherComment(comment.getFatherComment());
        this.setContents(comment.getContents());
        this.setCommentID(comment.getCommentID());
        this.setLocation(comment.getLocation());
        this.setParkingName(comment.getParkingName());
        this.setRelatedPlace(comment.getRelatedPlace());
    }
}
