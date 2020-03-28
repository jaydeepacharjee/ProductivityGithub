package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.CPojo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CRepo extends JpaRepository<CPojo, Long>{
	
	
    @Query(value="SELECT\r\n" + 
    		"        q.created_by,\r\n" + 
    		"        u.name as name,\r\n" + 
    		"        q.created_at as created_at,\r\n" + 
    		"        COUNT(q.error) as noOfError ,\r\n" + 
    		"        COUNT(DISTINCT q.scenario ) as noOfQuestion,\r\n" + 
    		"        COUNT(q.cstr) as noOfAttempt ,\r\n" + 
    		"        CAST(((COUNT(Distinct(q.scenario)))/(count(q.cstr)))*100 as decimal(5,\r\n" + 
    		"        2))as productivity       \r\n" + 
    		"    FROM\r\n" + 
    		"        users u      \r\n" + 
    		"    LEFT JOIN\r\n" + 
    		"        cpojo q              \r\n" + 
    		"            ON u.id=q.created_by        \r\n" + 
    		"    left join\r\n" + 
    		"        user_roles r              \r\n" + 
    		"            on u.id=r.user_id      \r\n" + 
    		"    WHERE\r\n" + 
    		"        r.role_id=1       \r\n" + 
    		"    GROUP BY\r\n" + 
    		"        u.id,\r\n" + 
    		"        DAY(q.created_at)       \r\n" + 
    		"    having\r\n" + 
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
"        m.feedback,\n" +
"        m.mentor_name,\n" +
"        m.created_at,\n" +
"		c.created_at as answerDate\n" +
"    from\n" +
"        cpojo c          \n" +
"    left join\n" +
"        mentor_feedback m         \n" +
"            on c.created_at=m.query_date     \n" +
"    inner join\n" +
"        question q               \n" +
"            on c.question_id=q.question_id      \n" +
"    inner join\n" +
"        scenario s              \n" +
"            on s.scenario_id=q.scenario_id      \n" +
"    inner join\n" +
"        function_table f              \n" +
"            on f.function_id=s.function_id      \n" +
"    inner join\n" +
"        domain d              \n" +
"            on d.domain_id=f.domain_id      \n" +
"    where\n" +
"        d.domain_id=?3          \n" +
"        and DATE(c.created_at)=DATE(?1)          \n" +
"        and c.created_by=?2     \n" +
"    order by\n" +
"        scenario;",nativeQuery = true  )
    public List<Object[]> generateReportAnalysis(String createdAt,Long createdBy,int domainId);
    

    @Query(value="select * from cpojo where created_at=?1 and question_id=?2",nativeQuery = true)
    public CPojo searchCRepo(String createdAt,int questionId);
    
    @Query(value="select\n" +
"        d.domain_id,\n" +
"        d.domain_name \n" +
"    from\n" +
"        cpojo q   \n" +
"    left join\n" +
"        mentor_feedback m \n" +
"            on q.created_at=m.query_date \n" +
"    inner join\n" +
"        question ques \n" +
"            on  q.question_id=ques.question_id \n" +
"    inner join\n" +
"        scenario s \n" +
"            on s.scenario_id=ques.scenario_id  \n" +
"    inner join\n" +
"        function_table f \n" +
"            on f.function_id=s.function_id \n" +
"    inner join\n" +
"        domain d \n" +
"            on d.domain_id=f.domain_id  \n" +
"    left join\n" +
"        question qus \n" +
"            on q.question_id=qus.question_id \n" +
"    where\n" +
"        DATE(q.created_at)=DATE(?2) \n" +
"        and q.created_by=?1 \n" +
"    order by\n" +
"        scenario;",nativeQuery = true)
    public List<Object[]> getDomainAnalsisRepo(long created_by, String createdAt);
}
