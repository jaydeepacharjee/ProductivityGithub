package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.Function;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Integer> {

//    @Query(value = "select count(*) from function where domain_id=?1 and function_name=?2", nativeQuery = true)
//    public Integer checkFunctionDuplicate(int domainId, String functionName);
    
    
    @Query(value="select count(*) from function where domain_id=?1 and function_name=?2",nativeQuery = true)
    public Integer checkDuplicate(int domainId,String functionName);

}
