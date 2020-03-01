package com.aroha.pet.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.JavaPojo;
import com.aroha.pet.model.QueryInfo;


public interface JavaDataRepo extends JpaRepository<JavaPojo, Long>{
	
	@Query("select new com.aroha.pet.repository.JavaRepo(n.id) from JavaPojo as n")
	public List<JavaRepo> getId();


}
