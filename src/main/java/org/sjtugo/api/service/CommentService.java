package org.sjtugo.api.service;


import com.vividsolutions.jts.geom.Location;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import org.sjtugo.api.DAO.CommentRepositoryJpa;
import org.sjtugo.api.DAO.Entity.MapVertexInfo;
import org.sjtugo.api.DAO.MapVertexInfoRepository;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.controller.ResponseEntity.CommentResponse;
import org.sjtugo.api.entity.Comment;
import org.sjtugo.api.controller.ResponseEntity.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentService {

    private final CommentRepositoryJpa commentRepositoryJpa;

    private final MapVertexInfoRepository mapVertexInfoRepository;

    private final UserRepository userRepository;

    static double r = 0.005;

    public CommentService(CommentRepositoryJpa commentRepositoryJpa,
                          MapVertexInfoRepository mapVertexInfoRepository,
                          UserRepository userRepository) {
        this.commentRepositoryJpa = commentRepositoryJpa;
        this.mapVertexInfoRepository = mapVertexInfoRepository;
        this.userRepository = userRepository;
    }

    /**
     *
     * @param loc:地点，前端传入，格式POINT(x y)
     * @param fatherID:父评论id
     * @return (x-r y+r)~(x+r y-r)范围内的评论列表
     */
    public List<CommentResponse> getCommentList(Point loc, Integer fatherID){

        double x = loc.getX();
        double y = loc.getY();
        double x1 = x-r;
        double x2 = x+r;
        double y1 = y-r;
        double y2 = y+r;
        String polygon = "POLYGON(("
                + x1 + " " + y1 + "," + x2 + " " + y1 + "," + x1 + " " + y2 + "," + x2 + " " + y2 + "," + x1 + " " + y1
                +"))";
        Polygon square;
        try {
            square = (Polygon) new WKTReader().read(polygon);
        } catch (ParseException e ){
            return new ArrayList<>();
        }
        return commentRepositoryJpa.findByLocationWithin(square,fatherID).stream()
                .filter(comment -> userRepository.findById(comment.getUserID()).isPresent())
                .map(comment -> {
                    CommentResponse result = new CommentResponse(comment);
                    result.setUserName(userRepository.findById(comment.getUserID()).orElseThrow().getName());
                    result.setApproveNames(
                            comment.getApproveUsers().stream()
                                    .filter(userid -> userRepository.findById(userid).isPresent())
                            .map(userid -> userRepository.findById(userid).orElseThrow().getName())
                            .collect(Collectors.toList())
                    );
                    return result;
                }).collect(Collectors.toList());
    }

    public List<Comment> getCommentList(Integer placeID){
        return commentRepositoryJpa.findByRelatedPlace(placeID);
    }

    /**
     * 添加评论/回复评论
     * @param contents 内容
     * @param userID 评论者ID
     * @param relatedPlace 评论相关位置ID
     * @param fatherID 父评论ID
     * @return 本次添加的新评论
     */
    public ResponseEntity<?> addComment( String contents, Integer userID, Integer relatedPlace, Integer fatherID) {
        Comment newComment = new Comment();
        newComment.setContents(contents);
        newComment.setUserID(userID);
        newComment.setCommentTime(LocalDateTime.now());
        try {
            MapVertexInfo parking = mapVertexInfoRepository.findById(relatedPlace).orElseThrow();
            newComment.setParkingName(parking.getVertexName()+"停车点");
            newComment.setLocation(parking.getLocation());
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(4,"MapVertexId invalid"),HttpStatus.BAD_REQUEST);
        }
        newComment.setRelatedPlace(relatedPlace);
        newComment.setSubComment(new ArrayList<>());
        newComment.setFatherComment(fatherID);
        commentRepositoryJpa.save(newComment);

        if (fatherID != 0){
            Comment fatherComment;
            try {
                fatherComment = commentRepositoryJpa.findById(fatherID).orElseThrow();
            } catch (Exception e){
                commentRepositoryJpa.delete(newComment);
                return new ResponseEntity<>(new ErrorResponse(5,"Invalid FatherID"),HttpStatus.BAD_REQUEST);
            }
            List<Integer> subComments = fatherComment.getSubComment();  //得到子评论
            subComments.add(newComment.getCommentID());
            fatherComment.setSubComment(subComments);
            commentRepositoryJpa.save(fatherComment);
        }
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    /**
     * 点赞
     * @param userID 点赞者ID
     * @param commentID 被点赞评论ID
     */
    public ResponseEntity<ErrorResponse> likeComment(Integer userID, Integer commentID) {
         Comment likedComment = commentRepositoryJpa.findById(commentID).orElse(null);  //get返回实体对象 获取被点赞的评论
         assert likedComment != null;
         try{
             List<Integer> approveUsers = likedComment.getApproveUsers();  //之前点赞的用户ID
             approveUsers.add(userID);  //添当前点赞用户ID
             likedComment.setApproveUsers(approveUsers);
             commentRepositoryJpa.save(likedComment);
             return new ResponseEntity<>(new ErrorResponse(0,"点赞+1"),HttpStatus.OK);
         } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(2,"No such comment!"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 返回子评论
     * @param fatherID:父评论ID
     * @return 子评论列表
     */
    public ResponseEntity<?> getSubCommentList(Integer fatherID){
        Comment fatherComment;
        try {
            fatherComment = commentRepositoryJpa.findById(fatherID).orElseThrow();
        } catch (Exception e){
            return new ResponseEntity<>(new ErrorResponse(3,"FatherID Not Found"),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(getCommentList(fatherComment.getLocation(),fatherID),HttpStatus.OK);
    }

    public ResponseEntity<ErrorResponse> deleteComment(Integer commentID) {
        Comment comment = commentRepositoryJpa.findById(commentID).orElse(null);
        assert comment != null;
        try {
            List<Integer> subCommentsID = comment.getSubComment();
            for (Integer ID : subCommentsID) {
                commentRepositoryJpa.deleteById(ID);
            }
            commentRepositoryJpa.deleteById(commentID);
            return new ResponseEntity<>(new ErrorResponse(0, "delete successfully!"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(2,"No such comment!"), HttpStatus.BAD_REQUEST);
        }
    }

}