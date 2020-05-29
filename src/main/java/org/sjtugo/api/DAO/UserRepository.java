package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByOpenid(String Openid);
    //更新数据需用sql语句，save无法部分更新
}
