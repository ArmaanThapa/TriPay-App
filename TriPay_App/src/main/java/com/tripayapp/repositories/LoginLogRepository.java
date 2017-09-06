package com.tripayapp.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.LoginLog;
import com.tripayapp.entity.User;
import com.tripayapp.model.Status;

public interface LoginLogRepository extends CrudRepository<LoginLog, Long>,
		JpaSpecificationExecutor<LoginLog> {

	@Query("select c from LoginLog c where c.created > CURRENT_DATE")
	List<LoginLog> findTodayEntry();

	@Query("select c.user from LoginLog c where c.status=?1")
	List<LoginLog> findLoginLogByStatus(Status status);

	@Query("select MAX(c.created) from LoginLog c where c.user=?1")
	Date findLastLoginDateOfUser(User user);

	@Query("select c from LoginLog c where c.user=?1 and c.created = (select MAX(d.created) FROM LoginLog d where d.user=?1 and d.status=?2)")
	LoginLog findLastLoginOfUser(User user,Status status);

	@Query("select c from LoginLog c where c.created > CURRENT_DATE and c.user=?1")
	List<LoginLog> findTodayEntryForUser(User user);

	@Query("select c from LoginLog c where c.created > CURRENT_DATE and c.user=?1 and c.status=?2")
	List<LoginLog> findTodayEntryForUserWithStatus(User user, Status status);
	
	@Modifying
	@Transactional
    @Query("update LoginLog c set c.status=?1 where c.id =?2")
	int deleteLoginLogForId(Status status, long id);
}
