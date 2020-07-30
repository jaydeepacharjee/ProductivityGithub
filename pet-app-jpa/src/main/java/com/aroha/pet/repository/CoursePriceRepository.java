package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.PriceModel;

@Repository
public interface CoursePriceRepository extends JpaRepository<PriceModel, Integer>{
	
}
