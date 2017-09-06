package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.DTHBillPaymentDTO;
import com.tripayapp.model.error.DTHBillPaymentError;
import com.tripayapp.repositories.PQServiceRepository;

public class DTHBillPaymentValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public DTHBillPaymentValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public DTHBillPaymentError checkError(DTHBillPaymentDTO dth) {
		DTHBillPaymentError error = new DTHBillPaymentError();
		boolean valid = true;
//		if (CommonValidation.isNull(dth.getServiceProvider())) {
//			error.setServiceProvider("Please select sevice provider");
//			valid = false;
//		}
//		if (CommonValidation.isNull(dth.getAmount())) {
//			error.setAmount("Please enter amount");
//			valid = false;
//		}
//		if (CommonValidation.isNull(dth.getDthNo())) {
//			error.setAmount("Please enter DTH number");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(dth.getDthNo())) {
//			error.setDthNo("Please enter valid DTH number");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(dth.getAmount())) {
//			error.setAmount("Please enter valid amount in the field");
//			valid = false;
//		}
		
		PQService service = pqServiceRepository.findServiceByCode(dth.getServiceProvider());
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(), dth.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. " + service.getMaxAmount());
			valid = false;
		}
		error.setValid(valid);
		return error;

	}

}
