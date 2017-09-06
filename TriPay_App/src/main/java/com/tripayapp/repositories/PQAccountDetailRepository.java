package com.tripayapp.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.PQAccountDetail;

public interface PQAccountDetailRepository extends CrudRepository<PQAccountDetail, Long>,
		PagingAndSortingRepository<PQAccountDetail, Long>, JpaSpecificationExecutor<PQAccountDetail> {

	@Query("select u from PQAccountDetail u")
	List<PQAccountDetail> findAll();

	@Query("select SUM(u.balance) from PQAccountDetail u")
	double getTotalBalance();

	@Query("select u.balance from PQAccountDetail u where u.id = ?1")
	double getUserWalletBalance(long id);

	@Modifying
	@Transactional
	@Query("update PQAccountDetail c set c.balance=?1 where c.id =?2")
	int updateUserBalance(double balance, long accountDetailId);

	@Query("select u from PQAccountDetail u")
	Page<PQAccountDetail> findAll(Pageable page);


	@Modifying
	@Transactional
	@Query("update PQAccountDetail c set c.points=?1 where c.id =?2")
	int updateUserPoints(long points, long id);


	@Modifying
	@Transactional
	@Query("update PQAccountDetail c set c.balance=?1 where c.id =?2")
	int updateUserBalance(String authority, long id);

	@Modifying
	@Transactional
	@Query("update PQAccountDetail a set a.points= a.points+?1 where a.accountNumber=?2")
	int addUserPoints(long points, long accountNumber);

	@Query("select u.balance from PQAccountDetail u where u.id=?1")
	double getBalanceByUserAccount(long accountDetailId);

}
