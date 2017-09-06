package com.tripayapp.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.BusDetails;
import com.tripayapp.entity.TravelBusDetail;

public interface BusDetailRepository extends CrudRepository<TravelBusDetail, Long>,
PagingAndSortingRepository<TravelBusDetail, Long>, JpaSpecificationExecutor<TravelBusDetail> {

	@Modifying
	@Transactional
	@Query("update TravelBusDetail c set c.apiRefNo=?1, c.blockId=?2, c.bookingDate=?3, c.clientId=?4 where c.id=?5")
	int updateTravelBusDetailByBusId(String apiRefNo, String blockId, Date bookingDate, String clientId, long id);

}
