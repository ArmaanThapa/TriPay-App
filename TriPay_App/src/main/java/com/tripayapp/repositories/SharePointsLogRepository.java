package com.tripayapp.repositories;


import com.tripayapp.entity.PQAccountDetail;
import com.tripayapp.entity.SessionLog;
import com.tripayapp.entity.SharePointsLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SharePointsLogRepository extends CrudRepository<SharePointsLog, Long>, JpaSpecificationExecutor<SharePointsLog> {
    @Query("select s from SharePointsLog s")
    List<SharePointsLog> getAllRequests();

    @Query("select s from SharePointsLog s where s.account=?1")
    List<SharePointsLog> getRequestByAccount(PQAccountDetail account);

    @Query("select s from SharePointsLog s where s.transactionRefNo=?1")
    SharePointsLog getByTransactionRefNo(String transactionRefNo);

}

