package com.tripayapp.repositories;

import com.tripayapp.entity.BusTripDetails;
import com.tripayapp.entity.PQAccountDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BusTripDetailsRepository extends CrudRepository<BusTripDetails,Long> ,PagingAndSortingRepository<BusTripDetails,Long>,JpaSpecificationExecutor<BusTripDetails>{

    @Query("SELECT t FROM BusTripDetails t where t.bookingRefNo = ?1")
    BusTripDetails findByBookingReference(String bookingRef);

    @Query("SELECT t FROM BusTripDetails t where t.blockingRefNo = ?1")
    BusTripDetails findByBlockingReference(String blockingRef);

    @Query("SELECT t FROM BusTripDetails t where t.account = ?1")
    List<BusTripDetails> findByAccount(PQAccountDetail account);

}
