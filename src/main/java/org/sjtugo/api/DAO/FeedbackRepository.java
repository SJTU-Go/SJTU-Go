package org.sjtugo.api.DAO;

import org.sjtugo.api.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
 //   @Modifying
//    @Transactional
//    void deleteByFeedbackID(Integer feedbackID);
}
