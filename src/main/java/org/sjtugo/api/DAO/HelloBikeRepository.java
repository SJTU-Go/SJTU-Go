package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.HelloBikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Table;

@Table(name = "helloBikeInfo")
public interface HelloBikeRepository extends JpaRepository<HelloBikeInfo,Integer> {
    int countAllByClusterPointEquals(String a);
}
