package com.aroha.pet.repository;

import com.aroha.pet.model.FeedBack;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jaydeep
 */
@Repository
public interface MentorFeedbackRepository extends JpaRepository<FeedBack, Integer> {

	@Query(value="select notification from mentor_feedback where learner_id=?1 order by created_at DESC LIMIT 1;",nativeQuery = true)
	Integer getLastNotification(Long learnerId);
	
	//List<FeedBack> findBylearnerId(Long id);
		
	@Query(value="select * from mentor_feedback where learner_id=?1 order by created_at desc",nativeQuery = true)
	List<FeedBack> getMentorFeedback(Long Id);
	
	
}
