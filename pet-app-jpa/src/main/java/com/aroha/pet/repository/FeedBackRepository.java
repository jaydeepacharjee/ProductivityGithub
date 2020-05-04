package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.QueryInfo;
import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<QueryInfo, Long> {

    
    @Query(value = " SELECT\n" +
"        q.created_by,\n" +
"        u.name as name,\n" +
"        q.created_at as created_at,\n" +
"        COUNT(q.exception_str) as noOfException ,\n" +
"        COUNT(DISTINCT q.scenario ) as noOfScenario,\n" +
"        COUNT(q.sql_str) as noOfSqlStr ,\n" +
"        CAST(100-((count(q.exception_str))/(count(q.sql_str)))*100 as decimal(5,\n" +
"        2))as productivity        \n" +
"    FROM\n" +
"        users u       \n" +
"     JOIN\n" +
"        query_info q               \n" +
"            ON u.id=q.created_by         \n" +
"     join\n" +
"        user_roles r               \n" +
"            on u.id=r.user_id       \n" +
"    WHERE\n" +
"        r.role_id=1        \n" +
"    GROUP BY\n" +
"        u.id,\n" +
"        date(q.created_at)        \n" +
"    having\n" +
"        count(distinct q.scenario)>0   \n" +
"    ORDER BY\n" +
"        created_at Desc", nativeQuery = true)
    public List<Object[]> getFeedBackStatus(); 
    
    @Query(value = "select * from query_info where created_at=?1 and question_id=?2 and created_by=?3", nativeQuery = true)
    public QueryInfo getFeedback(String createdAt, int qyestionId,Long createdBy);
    
    @Query(value="SELECT distinct d.domain_id,d.domain_name FROM domain d INNER JOIN function_table f ON d.domain_id=f.domain_id INNER JOIN scenario s ON f.function_id=\r\n" + 
    		"s.function_id INNER JOIN question q ON s.scenario_id=q.scenario_id INNER JOIN query_info q1 ON q1.question_id=q.question_id INNER JOIN users u\r\n" + 
    		"on u.id=q1.created_by WHERE DATE(q1.created_at)=DATE(?2) AND q1.created_by=?1 ORDER BY scenario;  \r\n" + 
    		"",nativeQuery = true)
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
