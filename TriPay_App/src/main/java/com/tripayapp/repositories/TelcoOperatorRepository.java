package com.tripayapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.TelcoOperator;

public interface TelcoOperatorRepository  extends CrudRepository<TelcoOperator, Long>, JpaSpecificationExecutor<TelcoOperator>   {

	@Query("select s from TelcoOperator s where s.code=?1")
	TelcoOperator findTelcoOperatorByCode(String code);
	
	@Query("select s from TelcoOperator s")
	List<TelcoOperator> findAllOperators();	
}
