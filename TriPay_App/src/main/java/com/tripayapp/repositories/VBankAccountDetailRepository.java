package com.tripayapp.repositories;

import com.tripayapp.entity.VBankAccountDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VBankAccountDetailRepository extends CrudRepository<VBankAccountDetail,Long>,JpaSpecificationExecutor<VBankAccountDetail>{

    @Query("SELECT v FROM VBankAccountDetail v where v.accountNumber=?1 AND v.mobileNumber=?2")
    VBankAccountDetail findByAccountNumberAndMobile(String accountNumber,String mobileNumber);

    @Query("SELECT v FROM VBankAccountDetail v where v.accountNumber = ?1")
    VBankAccountDetail findByAccountNumber(String accountNumber);
}
