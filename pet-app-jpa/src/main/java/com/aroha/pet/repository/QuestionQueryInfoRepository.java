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
    @Query(value = " select"
          +" d.domain_name,\n" +
"        f.function_name,\n" +
"        q.scenario,\n" +
"        q.sql_str,\n" +
"        q.exception_str,\n" +
"        q.created_at,\n" +
"        q.question_id,\n" +
"        m.feedback,\n" +
"        m.mentor_name,\n" +
"        m.created_at as feedbackDate,\n" +
"        qus.answer,\n" +
"        q.result_str as resultStr   \n" +
"    from\n" +
"        query_info q   \n" +
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
"        d.domain_id=?3 \n" +
"        and  DATE(q.created_at)=DATE(?2) \n" +
"        and q.created_by=?1 \n" +
"    order by\n" +
"        scenario;", nativeQuery = true)
    public List<Object[]> getReport(long created_by, String createdAt,int domainId);
//    public List<QuestionQueryInfo> getReport(long created_by, String createdAt, int questionId);

    @Query("select new com.aroha.pet.payload.QueryObject(q.exceptionStr) from "
            + "QueryInfo q where q.createdBy=?1 and Date(q.createdAt)=Date(?2)")
    public List<QueryObject> getException(long created_by, String createdAt);

}
