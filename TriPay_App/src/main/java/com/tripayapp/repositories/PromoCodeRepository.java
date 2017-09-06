package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.PromoCode;
import com.tripayapp.model.ServiceType;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Long>, PagingAndSortingRepository<PromoCode, Long>, JpaSpecificationExecutor<PromoCode>{

	@Query("select s from PromoCode s where s.id=?1")
	PromoCode findById(Long id);
//
//	@Query("select s from PromoCode s where s.serviceType=?1")
//	PromoCode findByValue(ServiceType service);
//
	@Query("select s from PromoCode s where s.promoCode=?1")
	PromoCode findByPromoCode(String code);
	
	
}
