package com.tripayapp.repositories;


import com.tripayapp.entity.BankDetails;
import com.tripayapp.entity.Banks;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BankDetailRepository extends CrudRepository<BankDetails, Long>, PagingAndSortingRepository<BankDetails,Long>,JpaSpecificationExecutor<BankDetails> {

    @Query("select u from BankDetails u where u.ifscCode=?1 AND u.bank=?2")
    BankDetails findByIfscCode(String ifscCode,Banks bank);

    @Query("select u.ifscCode from BankDetails u where u.bank=?1")
    List<String> getIFSCFromBank(Banks bank);
    
}
