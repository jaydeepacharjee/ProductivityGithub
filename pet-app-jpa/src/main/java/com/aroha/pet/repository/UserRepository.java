package com.aroha.pet.repository;

import com.aroha.pet.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

/**
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

//    Optional<User> findByEmail(String username, String email);
	List<User> findByIdIn(List<Long> userIds);

	//select * from users a, user_roles b,roles c where a.id = b.user_id and b.role_id = c.id and c.name = 'ROLE_ADMIN'
//	@Query("select user from User user where user.roles.name =:roleName")
	@Query(nativeQuery = true, value = "SELECT * \r\n" + 
			"FROM   users a, \r\n" + 
			"       user_roles b, \r\n" + 
			"       roles c \r\n" + 
			"WHERE  a.id = b.user_id \r\n" + 
			"       AND b.role_id = c.id \r\n" + 
			"       AND c.NAME =?\r\n" + 
			"       order by a.name asc")
	List<User> findByRoles(String roleName);

	Boolean existsByEmail(String email);
	
	
	@Query(value="select role_id from user_roles where user_id=?1",nativeQuery = true)
	public long getRole(long uId);
	
	
}
