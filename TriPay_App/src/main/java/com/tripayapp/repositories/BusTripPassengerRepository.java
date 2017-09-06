package com.tripayapp.repositories;

import com.tripayapp.entity.BusPassengerTrip;
import com.tripayapp.entity.BusTripDetails;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BusTripPassengerRepository extends CrudRepository<BusPassengerTrip,Long>,JpaSpecificationExecutor<BusPassengerTrip>,PagingAndSortingRepository<BusPassengerTrip,Long> {

    @Query("SELECT b FROM BusPassengerTrip b where b.busTripDetails = ?1")
    List<BusPassengerTrip> getAllPassengerByTrip(BusTripDetails tripDetails);

}
