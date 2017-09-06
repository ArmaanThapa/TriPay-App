package com.tripayapp.repositories;

import com.tripayapp.entity.BusDetails;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BusDetailsRepository extends CrudRepository<BusDetails,Long>,JpaSpecificationExecutor<BusDetails>,PagingAndSortingRepository<BusDetails,Long> {

    @Query("SELECT b FROM BusDetails b where b.boardingId = ?1")
    BusDetails getFromBoardingId(String boardingId);
}
