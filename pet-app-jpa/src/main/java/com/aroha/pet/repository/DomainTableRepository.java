package com.aroha.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.Domain;
import com.aroha.pet.model.QueryInfo;

@Repository
public interface DomainTableRepository extends JpaRepository<QueryInfo, Long> {

    @Query(value = " SELECT\r\n" + 
    		" 		  a.tech_id,\r\n" + 
    		" 		  a.technology_name,\r\n" + 
    		"        b.question_id,\r\n" + 
    		"        a.domain_name,\r\n" + 
    		"        a.function_name,\r\n" + 
    		"        a.scenario_title,\r\n" + 
    		"        b.question_desc,\r\n" + 
    		"        a.scenario_id               \r\n" + 
    		"    FROM\r\n" + 
    		"        (SELECT\r\n" + 
    		"            t.tech_id,\r\n" + 
    		"            t.technology_name,\r\n" + 
    		"            d.domain_id,\r\n" + 
    		"            d.domain_name,\r\n" + 
    		"            f.function_name,\r\n" + 
    		"            s.scenario_title,\r\n" + 
    		"            s.scenario_id,\r\n" + 
    		"            s.scenario_id AS sid                               \r\n" + 
    		"        FROM\r\n" + 
    		"            domain d                                      \r\n" + 
    		"        LEFT JOIN\r\n" + 
    		"            technology t                                                             \r\n" + 
    		"                ON t.tech_id = d.technology_id                                      \r\n" + 
    		"        LEFT JOIN\r\n" + 
    		"            function_table f                                                             \r\n" + 
    		"                ON d.domain_id = f.domain_id                                      \r\n" + 
    		"        LEFT JOIN\r\n" + 
    		"            scenario s                                                             \r\n" + 
    		"                ON f.function_id = s.function_id)a,\r\n" + 
    		"            (SELECT\r\n" + 
    		"                q.question_id,\r\n" + 
    		"                q.scenario_id AS sid,\r\n" + 
    		"                q.question_desc                               \r\n" + 
    		"        FROM\r\n" + 
    		"            question q)b               \r\n" + 
    		"    WHERE\r\n" + 
    		"        a.sid = b.sid", nativeQuery = true)
    public List<Object[]> getDomainData();
    


}
