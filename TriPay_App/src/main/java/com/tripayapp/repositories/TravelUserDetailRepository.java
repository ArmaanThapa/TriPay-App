package com.tripayapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.TravelUserDetail;

public interface TravelUserDetailRepository extends CrudRepository<TravelUserDetail, Long>,
PagingAndSortingRepository<TravelUserDetail, Long>{

}
