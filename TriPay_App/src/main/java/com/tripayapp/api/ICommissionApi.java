package com.tripayapp.api;

import java.util.List;

import com.tripayapp.entity.PQCommission;
import com.tripayapp.entity.PQService;

public interface ICommissionApi {
	
	PQCommission save(PQCommission commission);
	PQCommission findCommissionByIdentifier(String identifier);
	PQCommission findCommissionByType(String type);
	List<PQCommission> findCommissionByService(PQService service);
	PQCommission findCommissionByServiceAndAmount(PQService service, double amount);
	double getCommissionValue(PQCommission senderCommission, double amount);
	String createCommissionIdentifier(PQCommission commission);
}
