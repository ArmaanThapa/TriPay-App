package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.BankTransfer;
import com.tripayapp.entity.MBankTransfer;

public interface MBankTransferRepository extends CrudRepository<MBankTransfer, Long>,
										PagingAndSortingRepository<MBankTransfer,Long>,
										JpaSpecificationExecutor<MBankTransfer>{
	
	

}
