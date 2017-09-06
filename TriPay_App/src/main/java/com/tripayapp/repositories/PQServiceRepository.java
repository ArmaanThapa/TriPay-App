package com.tripayapp.repositories;

import com.tripayapp.entity.PQServiceType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.PQService;

import java.util.List;

public interface PQServiceRepository extends CrudRepository<PQService, Long>, JpaSpecificationExecutor<PQService> {

	@Query("select u from PQService u where u.code=?1")
	PQService findServiceByCode(String name);
	
	@Query("select u from PQService u where u.operatorCode=?1")
	PQService findServiceByOperatorCode(String name);

	@Query("select u from PQService u where u.serviceType.id=?1")
	List<PQService> findServiceByServiceTypeID(long id);


}
