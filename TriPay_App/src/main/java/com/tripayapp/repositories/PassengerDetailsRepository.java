package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.PassengerDetails;

public interface PassengerDetailsRepository extends CrudRepository<PassengerDetails, Long>,
PagingAndSortingRepository<PassengerDetails, Long>, JpaSpecificationExecutor<PassengerDetails> {
	

}
