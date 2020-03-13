package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.Scenario;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {

	
    @Query(value="select count(*) from scenario where function_id=?1 and scenario_title=?2",nativeQuery = true)
    public Integer checkDuplicate(int functionId,String scenarioName);
	
}
