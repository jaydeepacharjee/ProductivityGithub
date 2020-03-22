package com.aroha.pet.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.QueryInfo;

@Repository
public interface DomainTableRepository extends JpaRepository<QueryInfo, Long> {

    @Query(value = "SELECT\n" +
"         b.question_id,\n" +
"        a.domain_name,\n" +
"       a.function_name,\n" +
"        a.scenario_title,\n" +
"        b.question_desc,\n" +
"		  a.scenario_id \n" +
"    FROM\n" +
"        (SELECT\n" +
"            d.domain_id ,\n" +
"           d.domain_name,\n" +
"            f.function_name,\n" +
"           s.scenario_title,\n" +
"            s.scenario_id,\n" +
"           s.scenario_id AS sid   \n" +
"        FROM\n" +
"            domain d \n" +
"        LEFT JOIN\n" +
"            function_table f \n" +
"                ON d.domain_id = f.domain_id \n" +
"        left JOIN\n" +
"            scenario s \n" +
"                ON f.function_id = s.function_id)a,\n" +
"            (SELECT\n" +
"                q.question_id,\n" +
"                q.scenario_id AS sid ,\n" +
"                q.question_desc \n" +
"        FROM\n" +
"            question q )b  \n" +
"    WHERE\n" +
"        a.sid=b.sid;", nativeQuery = true)
    public List<Object[]> getDomainData();

}
