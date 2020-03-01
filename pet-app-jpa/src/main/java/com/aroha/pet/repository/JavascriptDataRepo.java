package com.aroha.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aroha.pet.model.JavascriptPojo;

public interface JavascriptDataRepo extends JpaRepository<JavascriptPojo, Long> {
	@Query("select new com.aroha.pet.repository.JavascriptRepo(n.id) from JavascriptPojo as n")
	public List<JavascriptRepo> getId();
}
