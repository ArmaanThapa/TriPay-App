package com.tripayapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tripayapp.entity.PQCommission;
import com.tripayapp.entity.PQService;

public interface PQCommissionRepository
		extends CrudRepository<PQCommission, Long>, JpaSpecificationExecutor<PQCommission> {

	@Query("select u from PQCommission u where u.identifier=?1")
	PQCommission findCommissionByIdentifier(String identifier);
	
	@Query("select u from PQCommission u where u.type=?1")
	PQCommission findCommissionByType(String type);
	
	@Query("select u from PQCommission u where u.service=?1")
	List<PQCommission> findCommissionByService(PQService service);

	@Query("SELECT u FROM PQCommission u WHERE u.service = ?1 AND u.minAmount <= ?2  AND u.maxAmount >= ?2")
	PQCommission findCommissionByServiceAndAmount(PQService service, double amount);
	
	@Query("SELECT u FROM PQCommission u WHERE u.id = ?1 AND u.minAmount <= ?2  AND u.maxAmount >= ?2")
	PQCommission getCommissionValue(long commissionId, double amount);
	
//	SELECT * FROM payqwikdb.pqcommission WHERE service_id = '111' AND minAmount <= '1000' AND maxAmount >= '1000';
	
//	@Modifying
//	@Transactional
//	@Query("update PQCommission c set c.value=?1 where c.service_id =?2")
//	int updateCommissionValue(double value, long serviceId);
}
