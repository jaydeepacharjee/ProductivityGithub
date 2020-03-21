package com.aroha.pet.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.JavaPojo;
import com.aroha.pet.model.QueryInfo;

@Repository
public interface JavaRepo extends JpaRepository<JavaPojo, Long>{

}
