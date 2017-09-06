package com.tripayapp.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.EmailLog;

public interface EmailLogRepository extends CrudRepository<EmailLog, Long>, PagingAndSortingRepository<EmailLog,Long>,JpaSpecificationExecutor<EmailLog> {

	@Query("select u from EmailLog u")
	Page<EmailLog> getAllEmails(Pageable page);
	
	@Query("SELECT u FROM EmailLog u where DATE(u.created) BETWEEN ?1 AND ?2")
	List<EmailLog> getDailyEmailLogBetween(Date from, Date to);

}
