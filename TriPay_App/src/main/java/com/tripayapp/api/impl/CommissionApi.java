package com.tripayapp.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.api.ICommissionApi;
import com.tripayapp.entity.PQCommission;
import com.tripayapp.entity.PQService;
import com.tripayapp.repositories.PQCommissionRepository;

public class CommissionApi implements ICommissionApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQCommissionRepository pqCommissionRepository;

	public CommissionApi(PQCommissionRepository pqCommissionRepository) {
		this.pqCommissionRepository = pqCommissionRepository;
	}

	@Override
	public PQCommission save(PQCommission commission) {
		return pqCommissionRepository.save(commission);
	}

	@Override
	public PQCommission findCommissionByIdentifier(String identifier) {
		PQCommission commission = new PQCommission();
		commission = pqCommissionRepository.findCommissionByIdentifier(identifier);
		return commission;
	}

	@Override
	public PQCommission findCommissionByType(String type) {
		PQCommission commission = new PQCommission();
		commission = pqCommissionRepository.findCommissionByType(type);
		return commission;
	}

	@Override
	public List<PQCommission> findCommissionByService(PQService service) {
		List<PQCommission> commission = new ArrayList<PQCommission>();
		logger.info("CommisionApi Service:" + service);
		commission = pqCommissionRepository.findCommissionByService(service);
		return commission;
	}

	@Override
	public PQCommission findCommissionByServiceAndAmount(PQService service, double amount) {
		PQCommission commission = pqCommissionRepository.findCommissionByServiceAndAmount(service, amount);
		if (commission == null) {
			System.err.print("commission is not null");
			commission = new PQCommission();
			commission.setMinAmount(1);
			commission.setMaxAmount(1000000);
			commission.setType("PRE");
			commission.setValue(0);
			commission.setFixed(true);
			commission.setService(service);
			commission.setIdentifier(createCommissionIdentifier(commission));
		}
		return commission;
	}

	@Override
	public double getCommissionValue(PQCommission senderCommission, double amount) {
		double netCommissionValue = 0;
		if (senderCommission.isFixed()) {
			netCommissionValue = senderCommission.getValue();
			logger.info("If Fixed then Before Commission value: " + netCommissionValue);
		} else {
			netCommissionValue = (senderCommission.getValue() * amount) / 100;
			logger.info("Percent Commission value: " + netCommissionValue);
		}
		String v = String.format("%.2f", netCommissionValue);
		netCommissionValue = Double.parseDouble(v);
		logger.info("Now Commission Value: " + netCommissionValue + "\n");
		return netCommissionValue;
	}

	@Override
	public String createCommissionIdentifier(PQCommission commission) {
		String identifier = commission.getType() + "|" + commission.getValue() + "|" + commission.getMinAmount() + "|"
				+ commission.getMaxAmount() + "|" + commission.isFixed() + "|" + commission.getService().getCode();
		return identifier;
	}


}
