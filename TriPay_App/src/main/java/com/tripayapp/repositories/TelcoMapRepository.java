package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.TelcoMap;

public interface TelcoMapRepository extends CrudRepository<TelcoMap, Long>, JpaSpecificationExecutor<TelcoMap> {

	@Query("select s from TelcoMap s where s.number=?1")
	TelcoMap findTelcoMapByNumber(String number);
	
	@Query("select count(u) from TelcoMap u")
	Long countTelcoMap();
	
}
