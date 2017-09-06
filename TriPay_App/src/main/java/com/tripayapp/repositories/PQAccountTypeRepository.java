package com.tripayapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.tripayapp.entity.PQAccountType;

public interface PQAccountTypeRepository extends CrudRepository<PQAccountType, Long>, JpaSpecificationExecutor<PQAccountType> {

	@Query("select u from PQAccountType u")
	List<PQAccountType> findAll();

	@Query("select u from PQAccountType u where u.code=?1")
	PQAccountType findByCode(String code);

}
