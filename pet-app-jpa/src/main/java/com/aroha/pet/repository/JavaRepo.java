package com.aroha.pet.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.CPojo;
import com.aroha.pet.model.DbInfo;
import com.aroha.pet.model.JavaPojo;
import com.aroha.pet.model.QueryInfo;

@Repository
public interface JavaRepo extends JpaRepository<JavaPojo, Long> {

    @Query(value = "SELECT\n" + 
    		"        q.created_by,\n" + 
    		"        u.name as name,\n" + 
    		"        q.created_at as created_at,\n" + 
    		"        COUNT(q.exceptionstr) as noOfError ,\n" + 
    		"        COUNT(DISTINCT q.scenario ) as noOfQuestion,\n" + 
    		"        COUNT(q.javastr) as noOfAttempt ,\n" + 
    		"        CAST(100-((COUNT(q.exceptionstr))/(count(q.javastr)))*100 as decimal(5,\n" + 
    		"        2))as productivity                  \n" + 
    		"    FROM\n" + 
    		"        users u                 \n" + 
    		"     JOIN\n" + 
    		"        java_pojo q                                         \n" + 
    		"            ON u.id=q.created_by                   \n" + 
    		"     join\n" + 
    		"        user_roles r                                         \n" + 
    		"            on u.id=r.user_id                 \n" + 
    		"    WHERE\n" + 
    		"        r.role_id =1                  \n" + 
    		"    GROUP BY\n" + 
    		"        u.id,\n" + 
    		"        date(q.created_at)                  \n" + 
    		"    having\n" + 
    		"        count(distinct q.scenario)>0     \n" + 
    		"    ORDER BY\n" + 
    		"        created_at Desc;", nativeQuery = true)
    public List<Object[]> generateReport();

    @Query(value = "select\r\n"
            + "        d.domain_name,\r\n"
            + "        f.function_name,\r\n"
            + "        s.scenario_title,\r\n"
            + "        c.javastr,\r\n"
            + "        c.exceptionstr,\r\n"
            + "        q.question_id,\r\n"
            + "        c.resultstr,\r\n"
            + "        c.scenario,\r\n"
            + "        m.feedback,\r\n"
            + "        m.mentor_name,\r\n"
            + "        m.created_at,\r\n"
            + "        c.created_at as answerDate     \r\n"
            + "    from\r\n"
            + "        java_pojo c                      \r\n"
            + "    inner join\r\n"
            + "        question q                            \r\n"
            + "            on c.question_id=q.question_id           \r\n"
            + "    inner join\r\n"
            + "        scenario s                           \r\n"
            + "            on s.scenario_id=q.scenario_id           \r\n"
            + "    inner join\r\n"
            + "        function_table f                           \r\n"
            + "            on f.function_id=s.function_id           \r\n"
            + "    inner join\r\n"
            + "        domain d                           \r\n"
            + "            on d.domain_id=f.domain_id\r\n"
            + " left join\r\n"
            + "        mentor_feedback m                      \r\n"
            + "            on c.created_at=m.query_date              \r\n"
            + "    where\r\n"
            + "        d.domain_id=?3                   \r\n"
            + "        and DATE(c.created_at)=DATE(?1)                   \r\n"
            + "        and c.created_by=?2          \r\n"
            + "    order by\r\n"
            + "        scenario;", nativeQuery = true)
    public List<Object[]> generateReportAnalysis(String createdAt, Long createdBy, int domainId);

    @Query(value = "select * from java_pojo where created_at=?1 and question_id=?2 and created_by=?3", nativeQuery = true)
    public JavaPojo searchJavaRepo(String createdAt, int questionId, Long createdBy);

    @Query(value = "SELECT DISTINCT d.domain_id, \n"
            + "                d.domain_name \n"
            + "FROM   domain d \n"
            + "       INNER JOIN function_table f \n"
            + "               ON d.domain_id = f.domain_id \n"
            + "       INNER JOIN scenario s \n"
            + "               ON f.function_id = s.function_id \n"
            + "       INNER JOIN question q \n"
            + "               ON s.scenario_id = q.scenario_id \n"
            + "       INNER JOIN java_pojo q1 \n"
            + "               ON q1.question_id = q.question_id \n"
            + "       INNER JOIN users u \n"
            + "               ON u.id = q1.created_by \n"
            + "WHERE  Date(q1.created_at) = Date(?2) \n"
            + "       AND q1.created_by = ?1 \n"
            + "ORDER  BY scenario; ", nativeQuery = true)
    public List<Object[]> getDomainAnalsisRepo(long created_by, String createdAt);

}
