package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.BankTransfer;
import com.tripayapp.entity.EmailLog;

public interface BankTransferRepository extends CrudRepository<BankTransfer, Long>, PagingAndSortingRepository<BankTransfer,Long>,JpaSpecificationExecutor<BankTransfer>{

}
