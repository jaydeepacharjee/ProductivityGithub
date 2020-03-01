package com.aroha.pet.repository;

import java.time.Instant;

import com.aroha.pet.model.QueryInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}
