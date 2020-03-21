package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.CPojo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CRepo extends JpaRepository<CPojo, Long>{
	
	
    @Query(value=" SELECT\n" +
"        q.created_by,\n" +
"        u.name as name,\n" +
"        q.created_at as created_at,\n" +
"        COUNT(q.error) as noOfError ,\n" +
"        COUNT(DISTINCT q.scenario ) as noOfQuestion,\n" +
"        COUNT(q.cstr) as noOfAttempt ,\n" +
"        CAST(((count(q.error))/(count(q.cstr)))*100 as decimal(5,\n" +
"        2))as productivity  \n" +
"    FROM\n" +
"        users u \n" +
"    LEFT JOIN\n" +
"        cpojo q \n" +
"            ON u.id=q.created_by   \n" +
"    left join\n" +
"        user_roles r \n" +
"            on u.id=r.user_id \n" +
"    WHERE\n" +
"        r.role_id=1  \n" +
"    GROUP BY\n" +
"        u.id,\n" +
"        DAY(q.created_at)  \n" +
"    having\n" +
"        count(distinct q.scenario)>0;",nativeQuery = true)
    public List<Object[]>generateReport();
    

}
