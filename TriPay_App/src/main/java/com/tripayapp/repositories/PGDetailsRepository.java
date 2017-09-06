package com.tripayapp.repositories;

import com.tripayapp.entity.PGDetails;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PGDetailsRepository extends CrudRepository<PGDetails, Long>,
        JpaSpecificationExecutor<PGDetails> {

    @Query("select md from PGDetails md where md.token=?1 ")
    PGDetails findByToken(String token);

    @Query("select md from PGDetails md where md.paymentGateway=?1 ")
    List<PGDetails> findMerchantsOfPaymentGateway(boolean paymentGateway);

    @Query("select md from PGDetails md where md.store=?1 ")
    List<PGDetails> findMerchantsOfStore(boolean store);


    @Query("select md from PGDetails md where md.user=?1")
    PGDetails findByUser(User u);

    @Query("select md.service from PGDetails md where md.user=?1")
    PQService findServiceByUser(User u);

    @Query("select md from PGDetails md")
    List<PGDetails> fetchAllDetails();

    @Query("select md.token from PGDetails md where md.user=?1")
    String findTokenByMerchant(User u);

    @Query("select md from PGDetails md where md.service=?1")
    PGDetails findDetailsByService(PQService service);

}
