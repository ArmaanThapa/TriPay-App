package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.PayAtStoreDTO;
import com.tripayapp.model.error.PayAtStoreError;
import com.tripayapp.repositories.PQServiceRepository;

public class PayStoreValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final PQServiceRepository pqServiceRepository;

	public PayStoreValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public PayAtStoreError checkError(PayAtStoreDTO store, String serviceCode) {
		boolean valid = true;
		PayAtStoreError error = new PayAtStoreError();
//		if (CommonValidation.isNull(store.getServiceProvider())) {
//			error.setServiceProvider("Please select service provider");
//			valid = false;
//		}
//		if (CommonValidation.isNull(store.getOrderID())) {
//			error.setOrderID("Please enter Order ID");
//			valid = false;
//		}
//		if (CommonValidation.isNull(store.getMessage())) {
//			error.setAmount("Enter Message in field");
//			valid = false;
//		}
//
//		if (CommonValidation.isNull(store.getAmount())) {
//			error.setAmount("Enter amount in field");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(store.getAmount())) {
//			error.setAmount("Amount must be in digits");
//			valid = false;
//		}
//
//		if (!CommonValidation.isNumeric(store.getOrderID())) {
//			error.setOrderID("Enter Valid Order ID");
//			valid = false;
//		}

		PQService service = pqServiceRepository.findServiceByCode(serviceCode);
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(),
				store.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. "
					+ service.getMaxAmount());
			valid = false;
		}

		error.setValid(valid);
		return error;
	}
}
