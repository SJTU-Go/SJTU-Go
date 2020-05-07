package org.sjtugo.api.dao;

import org.sjtugo.api.entity.Comment;
import org.springframework.data.repository.CrudRepository;


public interface CommentRepository extends CrudRepository<Comment, Integer> {

}