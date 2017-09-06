package com.tripayapp.repositories;

import com.tripayapp.entity.LocationDetails;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LocationDetailsRepository extends CrudRepository<LocationDetails,Long>,JpaSpecificationExecutor<LocationDetails> {

    @Query("SELECT l from LocationDetails l where l.pinCode = ?1")
    LocationDetails findLocationByPin(String pinCode);

}
