package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.TravelBusTransaction;

public interface BusTransactionRepository extends CrudRepository<TravelBusTransaction, Long>,
PagingAndSortingRepository<TravelBusTransaction, Long>{
	
	@Query("select u from TravelBusTransaction u where u.busTransactionRefNo=?1")
	TravelBusTransaction findByTransactionRefNo(String busTransactionRefNo);

}
