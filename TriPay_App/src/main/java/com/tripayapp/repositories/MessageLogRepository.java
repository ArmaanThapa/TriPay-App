package com.tripayapp.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.MessageLog;

public interface MessageLogRepository extends CrudRepository<MessageLog, Long>, PagingAndSortingRepository<MessageLog,Long>, JpaSpecificationExecutor<MessageLog> {

	@Query("select u from MessageLog u")
	Page<MessageLog> getAllMessage(Pageable page);
	
	@Query("SELECT u FROM MessageLog u where DATE(u.created) BETWEEN ?1 AND ?2")
	List<MessageLog> getDailyMessageLogBetween(Date from, Date to);
	
}
