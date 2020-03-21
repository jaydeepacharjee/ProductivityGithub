package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.CProgram;

@Repository
public interface CRepository extends JpaRepository<CProgram, Long> {

}
