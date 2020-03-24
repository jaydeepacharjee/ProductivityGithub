package com.aroha.pet.repository;

import com.aroha.pet.model.CPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.QueryInfo;
import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<QueryInfo, Long> {

    
    @Query(value = "SELECT q.created_by,u.name as name,\r\n"
            + "q.created_at as created_at,COUNT(q.exception_str) as noOfException ,\r\n"
            + "COUNT(DISTINCT q.scenario ) as noOfScenario, COUNT(q.sql_str) as noOfSqlStr ,\r\n"
            + "CAST(((count(q.exception_str))/(count(q.sql_str)))*100 as decimal(5,2))as productivity\r\n"
            + "FROM users u LEFT JOIN query_info q ON u.id=q.created_by \r\n"
            + "left join user_roles r on u.id=r.user_id WHERE r.role_id=1\r\n"
            + "GROUP BY u.id,DAY(q.created_at)\r\n"
            + "having count(distinct q.scenario)>0;", nativeQuery = true)
    public List<Object[]> getFeedBackStatus(); 
    
    @Query(value = "select * from query_info where created_at=?1 and question_id=?2", nativeQuery = true)
    public QueryInfo getFeedback(String createdAt, int qyestionId);
    
    @Query(value="select d.domain_id,d.domain_name from query_info q \r\n" + 
    		"left join  mentor_feedback m on q.created_at=m.query_date inner join question ques on\r\n" + 
    		"q.question_id=ques.question_id inner join scenario s on s.scenario_id=ques.scenario_id\r\n" + 
    		"inner join function_table f on f.function_id=s.function_id inner join domain d on d.domain_id=f.domain_id\r\n" + 
    		"left join question qus on q.question_id=qus.question_id where \r\n" + 
    		"DATE(q.created_at)=DATE(?2) and q.created_by=?1 order by scenario",nativeQuery = true)
    public List<Object[]> getDomainRepo(long created_by, String createdAt);
    
    
    @Query(value="select f.function_id ,f.function_name from query_info q \r\n" + 
    		"left join  mentor_feedback m on q.created_at=m.query_date inner join question ques on\r\n" + 
    		"q.question_id=ques.question_id inner join scenario s on s.scenario_id=ques.scenario_id\r\n" + 
    		"inner join function f on f.function_id=s.function_id inner join domain d on d.domain_id=f.domain_id\r\n" + 
    		"left join question qus on q.question_id=qus.question_id where \r\n" + 
    		"DATE(q.created_at)=DATE(?2) and q.created_by=?1 and d.domain_id =?3 order by scenario;",nativeQuery = true)
    public List<Object[]> getFunctionRepo(long created_by, String createdAt,int domainId);
    
    @Query(value="select s.scenario_id ,s.scenario_title from query_info q \r\n" + 
    		"left join  mentor_feedback m on q.created_at=m.query_date inner join question ques on\r\n" + 
    		"q.question_id=ques.question_id inner join scenario s on s.scenario_id=ques.scenario_id\r\n" + 
    		"inner join function f on f.function_id=s.function_id inner join domain d on d.domain_id=f.domain_id\r\n" + 
    		"left join question qus on q.question_id=qus.question_id where \r\n" + 
    		"DATE(q.created_at)=DATE(?2) and q.created_by=?1 and d.domain_id =?3 and f.function_id =?4 order by scenario;",nativeQuery = true)
    public List<Object[]> getScenarioRepo(long created_by, String createdAt,int domainId,int functionId);
    
    @Query(value="select qus.question_id,qus.question_desc \r\n" + 
    		"from\r\n" + 
    		"query_info q \r\n" + 
    		"left join  mentor_feedback m on q.created_at=m.query_date inner join question ques on\r\n" + 
    		"q.question_id=ques.question_id inner join scenario s on s.scenario_id=ques.scenario_id\r\n" + 
    		"inner join function f on f.function_id=s.function_id inner join domain d on d.domain_id=f.domain_id\r\n" + 
    		"left join question qus on q.question_id=qus.question_id where \r\n" + 
    		"DATE(q.created_at)=DATE(?2) and q.created_by=?1 and d.domain_id =?3 and "
    		+ "f.function_id =?4 and s.scenario_id =?5 order by scenario;",nativeQuery = true)
    public List<Object[]> getQuestionRepo(long created_by, String createdAt,int domainId,int functionId,int scenarioId);

 }
