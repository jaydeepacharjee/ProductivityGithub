package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.CPojo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CRepo extends JpaRepository<CPojo, Long>{
	
	
    @Query(value=" SELECT\n" +
"        q.created_by,\n" +
"        u.name as name,\n" +
"        q.created_at as created_at,\n" +
"        COUNT(q.error) as noOfError ,\n" +
"        COUNT(DISTINCT q.scenario ) as noOfQuestion,\n" +
"        COUNT(q.cstr) as noOfAttempt ,\n" +
"        CAST(((count(q.error))/(count(q.cstr)))*100 as decimal(5,\n" +
"        2))as productivity  \n" +
"    FROM\n" +
"        users u \n" +
"    LEFT JOIN\n" +
"        cpojo q \n" +
"            ON u.id=q.created_by   \n" +
"    left join\n" +
"        user_roles r \n" +
"            on u.id=r.user_id \n" +
"    WHERE\n" +
"        r.role_id=1  \n" +
"    GROUP BY\n" +
"        u.id,\n" +
"        DAY(q.created_at)  \n" +
"    having\n" +
"        count(distinct q.scenario)>0;",nativeQuery = true)
    public List<Object[]>generateReport();
    
    @Query(value="select\n" +
"        d.domain_name,\n" +
"        f.function_name,\n" +
"        s.scenario_title,\n" +
"        c.cstr,\n" +
"        c.error,\n" +
"        q.question_id,\n" +
"        c.resultstr,\n" +
"        c.scenario,\n" +
"		  m.feedback,\n" +
"		  m.mentor_name,\n" +
"		  m.created_at \n" +
"    from\n" +
"        cpojo c \n" +
"        left join mentor_feedback m\n" +
"        on c.created_at=m.query_date\n" +
"    inner join\n" +
"        question q  \n" +
"            on c.question_id=q.question_id \n" +
"    inner join\n" +
"        scenario s \n" +
"            on s.scenario_id=q.scenario_id \n" +
"    inner join\n" +
"        function_table f \n" +
"            on f.function_id=s.function_id \n" +
"    inner join\n" +
"        domain d \n" +
"            on d.domain_id=f.domain_id \n" +
"    where\n" +
"        d.domain_id=?3 \n" +
"        and DATE(c.created_at)=DATE(?1) \n" +
"        and c.created_by=?2 \n" +
"    order by\n" +
"        scenario;",nativeQuery = true  )
    public List<Object[]> generateReportAnalysis(String createdAt,Long createdBy,int domainId);
    

    @Query(value="select * from cpojo where created_at=?1 and question_id=?2",nativeQuery = true)
    public CPojo searchCRepo(String createdAt,int questionId);
}
