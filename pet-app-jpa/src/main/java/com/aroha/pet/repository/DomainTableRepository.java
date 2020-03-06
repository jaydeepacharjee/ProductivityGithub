package com.aroha.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.QueryInfo;




@Repository
public interface DomainTableRepository extends JpaRepository<QueryInfo, Long> {
	
	@Query(value="SELECT b.question_id,a.domain_name, a.function_name, a.scenario_title, b.question_desc FROM \r\n" + 
			"(SELECT d.domain_id ,d.domain_name, f.function_name, s.scenario_title,s.scenario_id AS sid  \r\n" + 
			"FROM domain d LEFT JOIN function f ON d.domain_id = f.domain_id  left JOIN scenario s ON f.function_id = s.function_id)a,\r\n" + 
			"(SELECT q.question_id,q.scenario_id AS sid ,q.question_desc FROM question q )b\r\n" + 
			"WHERE a.sid=b.sid;",nativeQuery = true)
	public List<Object[]> getDomainData();

}

