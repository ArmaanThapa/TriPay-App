package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.MobileTopupDTO;
import com.tripayapp.model.error.MobileTopupError;
import com.tripayapp.repositories.PQServiceRepository;

public class MobileTopupValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public MobileTopupValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public MobileTopupError checkError(MobileTopupDTO mobile) {
		MobileTopupError error = new MobileTopupError();
		boolean valid = true;
		PQService service = pqServiceRepository.findServiceByCode(mobile.getServiceProvider());
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(),
				mobile.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. "
					+ service.getMaxAmount());
			valid = false;
		}

		error.setValid(valid);
		return error;
	}
}
