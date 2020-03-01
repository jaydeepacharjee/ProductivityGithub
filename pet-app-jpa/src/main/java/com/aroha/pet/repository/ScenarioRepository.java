package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.Scenario;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Integer> {

	
	
}
