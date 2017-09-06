package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.LandlineBillPaymentDTO;
import com.tripayapp.model.error.LandlineBillPaymentError;
import com.tripayapp.repositories.PQServiceRepository;

public class LandlineBillPaymentValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public LandlineBillPaymentValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public LandlineBillPaymentError checkError(LandlineBillPaymentDTO landline) {

		LandlineBillPaymentError error = new LandlineBillPaymentError();
		boolean valid = true;
		
		PQService service = pqServiceRepository.findServiceByCode(landline.getServiceProvider());
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(), landline.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. " + service.getMaxAmount());
			valid = false;
		}
		error.setValid(valid);
		return error;

	}
}
