package com.aroha.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.JavascriptPojo;

@Repository
public interface JavascriptRepo extends JpaRepository<JavascriptPojo, Long> {
	
}
