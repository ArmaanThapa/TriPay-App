package com.tripayapp.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;


public interface UserSessionRepository extends CrudRepository<UserSession, String> {

	@Query("select s from UserSession s WHERE s.sessionId = ?1")
	UserSession findBySessionId(String sessionId);

	@Query("select s from UserSession s WHERE s.user.id = ?1 and s.expired = false")
	List<UserSession> getUserSessions(Long userId);

	@Query("select s from UserSession s WHERE s.user.id = ?1")
	List<UserSession> getUserSessionsIncludingExpired(Long userId);

	@Query("select distinct(s.user) from UserSession s WHERE s.expired=false")
	Page<User> findActiveUsers(Pageable page);

	@Query("select s from UserSession s WHERE s.expired=false")
	Page<UserSession> findActiveSessions(Pageable page);

	@Query("select count(s) from UserSession s WHERE s.expired=false")
	long countActiveSessions();

	@Query("select s from UserSession s WHERE s.sessionId = ?1 and s.expired = false")
	UserSession findByActiveSessionId(String sessionId);

	@Transactional
	@Modifying
	@Query("delete from UserSession s where s.expired=true or s.lastRequest < ?1")
	void deleteExpiredSessions(Date lastRefreshed);

	@Transactional
	@Modifying
	@Query("update UserSession s set s.lastRequest = now() where s.sessionId=?1")
	void refreshSession(String sessionId);

	@Query("select s from UserSession s where s.expired = false")
	List<UserSession> findActiveUser();

	@Query("select s from UserSession s WHERE s.user = ?1 and s.expired = false")
	UserSession findByActiveUserSession(User user);

}
