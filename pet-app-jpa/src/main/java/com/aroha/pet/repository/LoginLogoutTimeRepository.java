package com.aroha.pet.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.LoginLogoutTime;

@Repository
public interface LoginLogoutTimeRepository extends JpaRepository<LoginLogoutTime, Long> {

    public Optional<LoginLogoutTime> findByuserId(Long userId);

    @Query(value = "select * from login_logout_time where user_id=?1 order by login_date_time desc limit 1", nativeQuery = true)
    public Optional<LoginLogoutTime> findLatestUser(Long userId);

    @Query(value = "select *\n"
            + "from login_logout_time\n"
            + "where login_date_time in (select max(login_date_time)\n"
            + "                          from login_logout_time\n"
            + "					      group by user_id)", nativeQuery = true)
    public List<LoginLogoutTime> findLatestLoginTime();

    @Query(value = "select * from login_logout_time where user_id=?1 order by login_date_time desc limit 7", nativeQuery = true)
    public List<LoginLogoutTime> findLatestRecordOfUser(Long userId);

}
