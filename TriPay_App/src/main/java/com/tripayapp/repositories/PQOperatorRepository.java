package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.PQOperator;

public interface PQOperatorRepository extends CrudRepository<PQOperator, Long>, JpaSpecificationExecutor<PQOperator> {

	@Query("select u from PQOperator u where u.name=?1")
	PQOperator findOperatorByName(String name);
	
}
