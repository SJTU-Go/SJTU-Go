package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    //更新数据需用sql语句，save无法部分更新
}
