package com.tripayapp.repositories;

import com.tripayapp.entity.BankDetails;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.PromoCode;
import com.tripayapp.entity.PromoServices;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PromoServicesRepository extends CrudRepository<PromoServices, Long>, PagingAndSortingRepository<PromoServices,Long>,JpaSpecificationExecutor<PromoServices> {

    @Query("SELECT u FROM PromoServices u where u.promoCode=?1 AND u.service = ?2")
    PromoServices getByPromoAndService(PromoCode promoCode, PQService service);


    @Query("SELECT u FROM PromoServices u where u.promoCode=?1")
    List<PromoServices> getByPromoCode(PromoCode promoCode);


}
