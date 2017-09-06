package com.tripayapp.repositories;

import java.util.Date;
import java.util.List;

import com.tripayapp.entity.PQServiceType;
import com.tripayapp.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.PQAccountDetail;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.model.Status;

public interface PQTransactionRepository extends CrudRepository<PQTransaction, Long>,
		PagingAndSortingRepository<PQTransaction, Long>, JpaSpecificationExecutor<PQTransaction> {

	@Query("select u from PQTransaction u where u.transactionRefNo=?1")
	PQTransaction findByTransactionRefNo(String transactionRefNo);

	@Query("select u from PQTransaction u where u.transactionRefNo=?1 and u.status=?2")
	PQTransaction findByTransactionRefNoAndStatus(String transactionRefNo, Status status);

	@Query("select COUNT(u) from PQTransaction u where u.status=?1 AND u.transactionType = ?2")
	Long countTotalTransactionsByStatus(Status status,TransactionType transactionType);

	@Query("select COUNT(u) from PQTransaction u where DATE(u.created) = ?1 AND status = ?2 AND u.transactionType = ?3")
	long getDailyTransactionCount(Date date,Status status,TransactionType transactionType);

	@Query("select SUM(u) from PQTransaction u where DATE(u.created) = ?1 AND status = ?2 AND u.transactionType = ?3")
	double getDailyTransactionAmount(Date date,Status status,TransactionType transactionType);

	@Modifying
	@Transactional
	@Query("update PQTransaction c set c.status=?1, c.currentBalance=?2 where c.transactionRefNo =?3")
	int updateTransaction(Status status, double currentBalance, String transactionRefNo);

	@Modifying
	@Transactional
	@Query("update PQTransaction c set c.favourite=?2 where c.transactionRefNo =?1")
	int updateFavouriteTransaction(String transactionRefNo,boolean favourite);

	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 ")
	List<PQTransaction> getMonthlyTransaction(int year, int month, PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u where u.service IN ( ?1 , ?2 ) AND u.status = ?3")
	List<PQTransaction> getLoadMoneyTransactions(PQService ebs,PQService vnet,Status status);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where u.debit=?1 AND u.status=?2 ")
	double getValidAmount(boolean debit,Status status);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where u.service = ?1 AND u.status='Success' AND u.debit=false")
	double getValidAmountByService(PQService service);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where u.service = ?1 AND u.status='Success' AND u.debit=false AND DATE(u.created)=?2")
	double getValidAmountByServiceForDate(PQService service,Date date);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where u.account = ?1 AND u.status='Success' AND u.debit=false AND DATE(u.created)=?2")
	double getValidTopupAmountForDate(PQAccountDetail account,Date date);

	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4")
	List<PQTransaction> getDailyTransaction(int year, int month, int day, PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u where u.service=?1")
	List<PQTransaction> findTransactionByService(PQService service);

	@Query("SELECT u FROM PQTransaction u where u.created BETWEEN ?1 AND ?2  AND u.debit=?3 AND u.transactionType=?4 AND u.status=?5")
	List<PQTransaction> findTransactionByServiceAndDate(Date from, Date to, boolean debit, TransactionType transactionType, Status status);

	@Query("SELECT u FROM PQTransaction u where u.service=?1 AND u.debit=?2")
	List<PQTransaction> findTransactionByServiceAndDebit(PQService service,boolean debit);

	@Query("SELECT u FROM PQTransaction u where u.service=?1 AND u.status=?2")
	List<PQTransaction> findTransactionByServiceAndStatus(PQService service, Status status);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status = 'Success'")
	double getMonthlyTransactionTotal(int year, int month, PQAccountDetail account);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.debit = ?4 AND u.status = 'Success'")
	double getMonthlyTotalByCreditOrDebit(int year, int month, PQAccountDetail account,boolean debit);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.service = ?4 AND u.status = 'Success'")
	double getMonthlyTransactionTotalByServiceType(int year, int month, PQAccountDetail account, PQService service);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.status = 'Success'")
	double getDailyTransactionTotal(int year, int month, int day, PQAccountDetail account);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.debit = ?4 AND u.status = 'Success'")
	double getDailyTotalByCreditOrDebit(int year, int month, int day, PQAccountDetail account,boolean debit);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.debit = ?5")
	double getDailyDebitTransactionTotal(int year, int month, int day, PQAccountDetail account, boolean debit);

	@Query("SELECT u FROM PQTransaction u where  MONTH(created) = ?1")
	List<PQTransaction> getMonthlyTransaction(int month);

	@Query("SELECT u FROM PQTransaction u where DATE(u.created) BETWEEN ?1 AND ?2")
	List<PQTransaction> getDailyTransactionBetween(Date from, Date to);

	@Query("SELECT u FROM PQTransaction u where DATE(u.created) BETWEEN ?1 AND ?2")
	List<PQTransaction> getTransactionBetween(Date from, Date to);


	@Query("SELECT u FROM PQTransaction u where DATE(u.created) BETWEEN ?1 AND ?2 AND u.account=?3")
	List<PQTransaction> getDailyTransactionBetweenForAccount(Date from, Date to, PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u where DATE(u.created) = ?1")
	List<PQTransaction> getDailyTransactionByDate(Date from);

	@Query("SELECT count(u) FROM PQTransaction u where DATE(u.created) = ?1")
	Long countDailyTransactionByDate(Date from);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where DATE(u.created) = ?1")
	Double countDailyTransactionAmountByDate(Date from);

	@Query("SELECT MAX(t.created) FROM PQTransaction t where t.account=?1")
	Date getTimeStampOfLastTransaction(PQAccountDetail account);

	@Query("SELECT MAX(t.created) FROM PQTransaction t where t.account=?1 AND t.status=?2")
	Date getTimeStampOfLastTransactionByStatus(PQAccountDetail account,Status status);


	@Query("SELECT u FROM PQTransaction u where DATE(u.created) = ?1 AND u.account=?2")
	List<PQTransaction> getDailyTransactionByDateForAccount(Date from, PQAccountDetail account);

	@Query("SELECT count(u) FROM PQTransaction u where DATE(u.created) = ?1 AND u.account=?2")
	Long countDailyTransactionByDateForAccount(Date from, PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u where u.account= ?1")
	List<PQTransaction> getTotalTransactions(PQAccountDetail account);
	
	@Query("SELECT u FROM PQTransaction u where u.account= ?1 AND u.status='Reversed' ORDER BY u.id DESC ")
	List<PQTransaction> getReverseTransactionsOfUser(PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u where u.account= ?1 AND u.status='Reversed' AND u.created=(select MAX(f.created) from PQTransaction f where f.account=?1)")
	PQTransaction getLastReverseTransactionsOfUser(PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u")
	List<PQTransaction> getTotalTransactions();

	@Query("select u from PQTransaction u")
	Page<PQTransaction> findAll(Pageable page);

	@Query("select u from PQTransaction u where u.account = ?1")
	Page<PQTransaction> findAllByAccount(Pageable page,PQAccountDetail account);

	@Query("select u from PQTransaction u where u.account=?1")
	Page<PQTransaction> findAllTransactionByUser(Pageable page, PQAccountDetail account);
	
	@Query("select u from PQTransaction u where u.account=?1")
	List<PQTransaction> findAllTransactionByMerchant(PQAccountDetail account);


	@Query("select u from PQTransaction u where u.account=?1 AND u.status=?2 AND u.transactionRefNo LIKE '%D'")
	List<PQTransaction> findAllTransactionByUserAndStatus(PQAccountDetail account,Status status);

	@Transactional
	@Query("SELECT u FROM PQTransaction u where DATE(u.created) BETWEEN ?1 AND ?2 AND u.account=?3 AND u.amount=?4")
	List<PQTransaction> findTransactionByAccount(Date from, Date to, PQAccountDetail account, double amount);

	@Transactional
	@Query("SELECT u FROM PQTransaction u where DATE(u.created) BETWEEN ?1 AND ?2 AND u.account=?3 AND u.amount>=?4")
	List<PQTransaction> findTransactionByAccountAndAmount(Date from, Date to, PQAccountDetail account, double amount);

	@Query("SELECT u FROM PQTransaction u where u.account=?1")
	List<PQTransaction> findTransactionByAccount(PQAccountDetail account);

//	@Transactional
//	@Query("SELECT t FROM PQTransaction t LEFT JOIN t.service s WHERE s.code=?1 AND t.account=?2")
//	List<PQTransaction> findTransactionDateByAccountAndService(String serviceType, PQAccountDetail account);
	
	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.debit = ?5")
	List<PQTransaction> getDailyDebitTransaction(int year, int month, int day, PQAccountDetail account, boolean debit);
	
	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status='Success' AND u.debit=true")
	List<PQTransaction> getMonthlyDebitTransaction(int year, int month, PQAccountDetail account);

	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.status='Success' AND u.debit=true OR u.debit=false")
	List<PQTransaction> getDailyCreditAndDebitTransation(int year, int month, int day, PQAccountDetail account);
	
	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status='Success' AND u.debit=true OR u.debit=false")
	List<PQTransaction> getMonthlyCreditAndDebitTransation(int year, int month, PQAccountDetail account);
	
	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.status='Success' AND u.debit=false")
	List<PQTransaction> getDailyCreditTransation(int year, int month, int day, PQAccountDetail account);
	
	@Query("SELECT u FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status='Success' AND u.debit=false")
	List<PQTransaction> getMonthlyCreditTransation(int year, int month, PQAccountDetail account);
	
	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.debit=true")
	double getDailyDebitTransactionTotalAmount(int year, int month, int day, PQAccountDetail account);
	
	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status='Success' AND u.debit=true")
	double getMonthlyDebitTransactionTotalAmount(int year, int month, PQAccountDetail account);

	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.status='Success' AND u.debit=true OR u.debit=false")
	double getDailyCreditAndDebitTransationTotalAmount(int year, int month, int day, PQAccountDetail account);
	
	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status='Success' AND u.debit=true OR u.debit=false")
	double getMonthlyCreditAndDebitTransationTotalAmount(int year, int month, PQAccountDetail account);
	
	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND DAY(u.created) = ?3 AND u.account = ?4 AND u.status='Success' AND u.debit=false")
	double getDailyCreditTransationTotalAmount(int year, int month, int day, PQAccountDetail account);
	
	@Query("SELECT SUM(u.amount) FROM PQTransaction u where YEAR(u.created) = ?1 AND MONTH(u.created) = ?2 AND u.account = ?3 AND u.status='Success' AND u.debit=false")
	double getMonthlyCreditTransationTotalAmount(int year, int month, PQAccountDetail account);
	
	@Query("SELECT u FROM PQTransaction u where u.account= ?1 AND u.status='Success'")
	List<PQTransaction> getTotalSuccessTransactions(PQAccountDetail account);
//
	@Query("select t from PQTransaction t where t.service=?1 AND (t.transactionRefNo LIKE '%C' OR t.transactionRefNo LIKE '%D')")
	List<PQTransaction> findAllTransactionForMerchant(PQService service);

}
