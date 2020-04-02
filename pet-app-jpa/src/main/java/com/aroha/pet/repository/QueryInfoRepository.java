package com.aroha.pet.repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aroha.pet.model.QueryInfo;

/**
 * //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
 *
 * @author Sony George | Date : 13 Mar, 2019 4:33:42 PM
 */
@Repository
public interface QueryInfoRepository extends JpaRepository<QueryInfo, Long> {

	public List<QueryInfo> findAllByCreatedBy(Long id);

	public List<QueryInfo> findAllByCreatedByAndCreatedAtGreaterThanEqual(Long id, Instant fromDate);

	public List<QueryInfo> findAllByCreatedByAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Long id, Instant fromDate, Instant toDate);

	public List<QueryInfo> findAllByCreatedByAndCreatedAtLessThanEqual(Long id, Instant toDate);
	
	@Query(value="select COUNT(*) FROM query_info q WHERE DATE(q.created_at)=DATE(?2) "
			+ "AND q.created_by=?3 AND q.sql_str=?4\r\n" + 
			"AND q.question_id=?1",nativeQuery = true)
	public Integer checkDuplicateQuery(int questionId,Date date,Long userId,String sqlStr);

}
