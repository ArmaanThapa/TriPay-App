package com.tripayapp.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.MRequestLog;

public interface MRequestLogRepository extends CrudRepository<MRequestLog, Long>,
        JpaSpecificationExecutor<MRequestLog> {

}
