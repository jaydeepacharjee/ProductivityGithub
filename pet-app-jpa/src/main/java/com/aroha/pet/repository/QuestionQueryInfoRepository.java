package com.aroha.pet.repository;

import com.aroha.pet.model.QueryInfo;
import com.aroha.pet.payload.QueryObject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jaydeep
 */
@Repository
public interface QuestionQueryInfoRepository extends JpaRepository<QueryInfo, Integer> {

    /*
    @Query(value = "select q.id as id,q.scenario,q.sql_str,q.exception_str,q.created_at,q.question_id,m.feedback,"
            + "m.mentor_name,m.created_at as feedback_date,qus.answer,q.result_str as resultStr from \n"
            + "query_info q left join mentor_feedback m on q.created_at=m.query_date left join question qus on q.question_id=qus.question_id where  q.question_id=?3 and\n" 
            + "DATE(q.created_at)=DATE(?2)and q.created_by=?1 order by scenario", nativeQuery = true)
    public List<QuestionQueryInfo> getReport(long created_by, String createdAt,int questionId);
     */
    @Query(value = " select\r\n" + 
    		"        d.domain_name,\r\n" + 
    		"        f.function_name,\r\n" + 
    		"        q.scenario,\r\n" + 
    		"        q.sql_str,\r\n" + 
    		"        q.exception_str,\r\n" + 
    		"        q.created_at,\r\n" + 
    		"        q.question_id,\r\n" + 
    		"        m.feedback,\r\n" + 
    		"        m.mentor_name,\r\n" + 
    		"        m.created_at as feedbackDate,\r\n" + 
    		"        qus.answer,\r\n" + 
    		"        q.result_str as resultStr        \r\n" + 
    		"    from\r\n" + 
    		"        query_info q        \r\n" + 
    		"\r\n" + 
    		"    inner join\r\n" + 
    		"        question ques              \r\n" + 
    		"            on  q.question_id=ques.question_id      \r\n" + 
    		"    inner join\r\n" + 
    		"        scenario s              \r\n" + 
    		"            on s.scenario_id=ques.scenario_id       \r\n" + 
    		"    inner join\r\n" + 
    		"        function_table f              \r\n" + 
    		"            on f.function_id=s.function_id      \r\n" + 
    		"    inner join\r\n" + 
    		"        domain d              \r\n" + 
    		"            on d.domain_id=f.domain_id       \r\n" + 
    		"    left join\r\n" + 
    		"        question qus              \r\n" + 
    		"            on q.question_id=qus.question_id\r\n" + 
    		"	  left join\r\n" + 
    		"        mentor_feedback m              \r\n" + 
    		"            on q.created_at=m.query_date            \r\n" + 
    		"    where\r\n" + 
    		"        d.domain_id=?3          \r\n" + 
    		"        and  DATE(q.created_at)=DATE(?2)          \r\n" + 
    		"        and q.created_by=?1      \r\n" + 
    		"    order by\r\n" + 
    		"        scenario;", nativeQuery = true)
    public List<Object[]> getReport(long created_by, String createdAt,int domainId);
//    public List<QuestionQueryInfo> getReport(long created_by, String createdAt, int questionId);

    @Query("select new com.aroha.pet.payload.QueryObject(q.exceptionStr) from "
            + "QueryInfo q where q.createdBy=?1 and Date(q.createdAt)=Date(?2)")
    public List<QueryObject> getException(long created_by, String createdAt);

}
