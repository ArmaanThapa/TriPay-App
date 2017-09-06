package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.GasBillPaymentDTO;
import com.tripayapp.model.error.GasBillPaymentError;
import com.tripayapp.repositories.PQServiceRepository;

public class GasBillPaymentValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public GasBillPaymentValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public GasBillPaymentError checkError(GasBillPaymentDTO gas) {
		GasBillPaymentError error = new GasBillPaymentError();
		boolean valid = true;
//		if (CommonValidation.isNull(gas.getAccountNumber())) {
//			error.setAccountNumber("Please enter Account Number");
//			valid = false;
//		}
//		if (CommonValidation.isNull(gas.getAmount())) {
//			error.setAmount("Please enter Amount");
//			valid = false;
//		}
//		if (CommonValidation.isNull(gas.getServiceProvider())) {
//			error.setServiceProvider("Please select Service Provider");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(gas.getAccountNumber())) {
//			error.setAccountNumber("Enter valid account number");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(gas.getAmount())) {
//			error.setAmount("Enter valid amount in the field");
//			valid = false;
//		}
		
		PQService service = pqServiceRepository.findServiceByCode(gas.getServiceProvider());
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(), gas.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. " + service.getMaxAmount());
			valid = false;
		}
		error.setValid(valid);
		return error;
	}
}
