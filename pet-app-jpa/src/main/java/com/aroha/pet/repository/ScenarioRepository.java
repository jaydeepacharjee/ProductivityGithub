package com.aroha.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.Scenario;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {

	
    @Query(value="select * from scenario where function_id=?1",nativeQuery = true)
    public List<Scenario> checkDuplicate(int functionId);
	
}
