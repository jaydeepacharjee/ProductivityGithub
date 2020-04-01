package com.aroha.pet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.CPojo;
import com.aroha.pet.model.JavascriptPojo;

@Repository
public interface JavascriptRepo extends JpaRepository<JavascriptPojo, Long> {

    @Query(value = "SELECT\r\n"
            + "        q.created_by,\r\n"
            + "        u.name as name,\r\n"
            + "        q.created_at as created_at,\r\n"
            + "        COUNT(q.error) as noOfError ,\r\n"
            + "        COUNT(DISTINCT q.scenario ) as noOfQuestion,\r\n"
            + "        COUNT(q.javascriptstr) as noOfAttempt ,\r\n"
            + "        CAST(((COUNT(Distinct(q.scenario)))/(count(q.javascriptstr)))*100 as decimal(5,\r\n"
            + "        2))as productivity       \r\n"
            + "    FROM\r\n"
            + "        users u      \r\n"
            + "    LEFT JOIN\r\n"
            + "        javascript_pojo q              \r\n"
            + "            ON u.id=q.created_by        \r\n"
            + "    left join\r\n"
            + "        user_roles r              \r\n"
            + "            on u.id=r.user_id      \r\n"
            + "    WHERE\r\n"
            + "        r.role_id =1       \r\n"
            + "    GROUP BY\r\n"
            + "        u.id,\r\n"
            + "        DAY(q.created_at)       \r\n"
            + "    having\r\n"
            + "        count(distinct q.scenario)>0;", nativeQuery = true)
    public List<Object[]> generateReport();

    @Query(value = "select\r\n"
            + "        d.domain_name,\r\n"
            + "        f.function_name,\r\n"
            + "        s.scenario_title,\r\n"
            + "        c.javascriptstr,\r\n"
            + "        c.error,\r\n"
            + "        q.question_id,\r\n"
            + "        c.resultstr,\r\n"
            + "        c.scenario,\r\n"
            + "        m.feedback,\r\n"
            + "        m.mentor_name,\r\n"
            + "        m.created_at,\r\n"
            + "        c.created_at as answerDate     \r\n"
            + "    from\r\n"
            + "        javascript_pojo c                      \r\n"
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

    @Query(value = "select * from javascript_pojo where created_at=?1 and question_id=?2", nativeQuery = true)
    public JavascriptPojo searchJavascriptRepo(String createdAt, int questionId);

    @Query(value = " select\r\n"
            + "        d.domain_id,\r\n"
            + "        d.domain_name      \r\n"
            + "    from\r\n"
            + "        javascript_pojo q                    \r\n"
            + "                \r\n"
            + "    inner join\r\n"
            + "        question ques              \r\n"
            + "            on  q.question_id=ques.question_id      \r\n"
            + "    inner join\r\n"
            + "        scenario s              \r\n"
            + "            on s.scenario_id=ques.scenario_id       \r\n"
            + "    inner join\r\n"
            + "        function_table f              \r\n"
            + "            on f.function_id=s.function_id      \r\n"
            + "    inner join\r\n"
            + "        domain d              \r\n"
            + "            on d.domain_id=f.domain_id       \r\n"
            + "    left join\r\n"
            + "        question qus              \r\n"
            + "            on q.question_id=qus.question_id \r\n"
            + " left join\r\n"
            + "        mentor_feedback m \r\n"
            + "    on q.created_at=m.query_date      \r\n"
            + "    where\r\n"
            + "        DATE(q.created_at)=DATE(?2)          \r\n"
            + "        and q.created_by=?1      \r\n"
            + "    order by\r\n"
            + "        scenario;\r\n"
            + "", nativeQuery = true)
    public List<Object[]> getDomainAnalsisRepo(long created_by, String createdAt);

}
