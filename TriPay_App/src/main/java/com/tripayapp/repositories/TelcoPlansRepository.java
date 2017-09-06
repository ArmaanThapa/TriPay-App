package com.tripayapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.TelcoPlans;

public interface TelcoPlansRepository  extends CrudRepository<TelcoPlans, Long>, JpaSpecificationExecutor<TelcoPlans> {
	
	@Query("select u from TelcoPlans u where u.operator.code=?1 AND u.circle.code=?2 AND u.planName=?3 AND u.amount=?4")
	TelcoPlans findTelcoPlans(String operator, String circle, String planName, String amount);
	
	@Query("select count(u) from TelcoPlans u")
	Long countTelcoPlans();
	
	@Query("select u from TelcoPlans u where u.operator.code=?1 AND u.circle.code=?2")
	List<TelcoPlans> findTelcoPlansByOperatorCircle(String operator, String circle);


}
