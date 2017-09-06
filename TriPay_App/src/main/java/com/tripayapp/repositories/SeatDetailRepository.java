package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.TravelSeatDetail;

public interface SeatDetailRepository extends CrudRepository<TravelSeatDetail, Long> ,
PagingAndSortingRepository<TravelSeatDetail, Long>,JpaSpecificationExecutor<TravelSeatDetail>{

}
