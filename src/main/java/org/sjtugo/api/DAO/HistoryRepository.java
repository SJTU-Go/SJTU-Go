package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    List<History> findByUserID(Integer userID);
}
