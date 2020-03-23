package com.aroha.pet.repository;

import com.aroha.pet.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jaydeep 
 */
@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Integer>{

}
