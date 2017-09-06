package com.tripayapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.TelcoCircle;

public interface TelcoCircleRepository extends CrudRepository<TelcoCircle, Long>, JpaSpecificationExecutor<TelcoCircle>  {

	@Query("select s from TelcoCircle s where s.code=?1")
	TelcoCircle findTelcoCircleByCode(String code);
	
	@Query("select s from TelcoCircle s")
	List<TelcoCircle> findAllCircles();
	
	
}
