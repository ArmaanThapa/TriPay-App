package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tripayapp.entity.PromoCode;
import com.tripayapp.entity.RedeemCode;
import com.tripayapp.entity.User;

public interface RedeemCodeRepository extends CrudRepository<RedeemCode, Long>, PagingAndSortingRepository<RedeemCode, Long>, JpaSpecificationExecutor<RedeemCode> {

//	@Query("select p from RedeemCode p where p.user=?1 AND p.promoCode=?2")
//	RedeemCode findByPromoCodeAndUser(long user_id , long promoCode_id);
	
	@Query("select p from RedeemCode p where p.user=?1 AND p.promoCode=?2")
	RedeemCode findByPromoCodeAndUser(User user , PromoCode code);
}
