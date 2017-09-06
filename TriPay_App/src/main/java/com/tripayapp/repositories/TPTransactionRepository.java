package com.tripayapp.repositories;

import com.tripayapp.entity.TPTransaction;
import com.tripayapp.entity.TelcoPlans;
import com.tripayapp.entity.User;
import com.tripayapp.model.Status;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TPTransactionRepository extends CrudRepository<TPTransaction, Long>, JpaSpecificationExecutor<TPTransaction> {

    @Query("select t from TPTransaction t where t.orderId = ?1")
    TPTransaction findByOrderId(String orderId);

    @Query("select t from TPTransaction t where t.orderId = ?1 AND t.merchant = ?2")
    TPTransaction findByOrderIdAndMerchant(String orderId,User merchant);


    @Query("select t from TPTransaction t where t.transactionRefNo = ?1")
    TPTransaction findByTransactionRefNo(String transactionRefNo);

    @Modifying
    @Transactional
    @Query("update TPTransaction t set t.status = ?2 where t.transactionRefNo = ?1")
    int updateTransactionStatus(String transactionRefNo,Status status);

    @Query("select t from TPTransaction t where t.merchant = ?1")
    List<TPTransaction> findByMerchant(User u);
}
