package com.aroha.pet.repository;

import com.aroha.pet.model.DbInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Sony George | Date : 13 Mar, 2019 3:34:32 PM
 */
public interface DBRepository extends JpaRepository<DbInfo, Long> {

	public Page<DbInfo> findByCreatedBy(Long id, Pageable pageable);
	public List<DbInfo> findAllByCreatedBy(Long id);
	
	@Query(value="select * from db_info",nativeQuery = true)
	public List<DbInfo> findDbFromID();

}
