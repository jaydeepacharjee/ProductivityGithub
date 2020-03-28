package com.aroha.pet.repository;

import com.aroha.pet.model.Technology;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jaydeep 
 */
@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Integer>{
	
    @Query(value="select technology_id as tech_id,technology_name from mentor_feedback where learner_id=?1",nativeQuery = true)
    public List<Technology> getTech(Long userId);
}
