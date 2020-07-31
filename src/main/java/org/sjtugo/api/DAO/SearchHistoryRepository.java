package org.sjtugo.api.DAO;

import org.sjtugo.api.DAO.Entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
}
