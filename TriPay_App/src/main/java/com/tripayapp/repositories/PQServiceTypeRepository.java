package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.PQServiceType;

public interface PQServiceTypeRepository extends
		CrudRepository<PQServiceType, Long>,
		JpaSpecificationExecutor<PQServiceType> {

	@Query("select u from PQServiceType u where u.name=?1")
	PQServiceType findServiceTypeByName(String name);

}
