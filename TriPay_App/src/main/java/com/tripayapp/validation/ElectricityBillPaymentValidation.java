package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.ElectricityBillPaymentDTO;
import com.tripayapp.model.error.ElectricityBillPaymentError;
import com.tripayapp.repositories.PQServiceRepository;

public class ElectricityBillPaymentValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public ElectricityBillPaymentValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public ElectricityBillPaymentError checkError(ElectricityBillPaymentDTO electricity) {
		ElectricityBillPaymentError error = new ElectricityBillPaymentError();
		boolean valid = true;
//		if (CommonValidation.isNull(electricity.getAccountNumber())) {
//			error.setAccountNumber("Please enter account number");
//			valid = false;
//		}
//		if (CommonValidation.isNull(electricity.getAmount())) {
//			error.setAmount("Please enter amount");
//			valid = false;
//		}
//		if (CommonValidation.isNull(electricity.getServiceProvider())) {
//			error.setAccountNumber("Please select service provider");
//			valid = false;
//		}
//		if (electricity.getServiceProvider().equalsIgnoreCase("REE")) {
//			if (CommonValidation.isNull(electricity.getCycleNumber())) {
//				error.setCycleNumber("Please enter cycle number");
//				valid = false;
//			}
//			if (!CommonValidation.isNumeric(electricity.getCycleNumber())) {
//				error.setCycleNumber("Enter valid cycle number");
//				valid = false;
//			}
//		}
//		if (!CommonValidation.isNumeric(electricity.getAmount())) {
//			error.setAmount("Enter valid Amount");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(electricity.getAccountNumber())) {
//			error.setAccountNumber("Enter valid account number");
//			valid = false;
//		}

		PQService service = pqServiceRepository.findServiceByCode(electricity.getServiceProvider());
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(),
				electricity.getAmount())) {
			error.setAmount(
					"Amount should be between Rs. " + service.getMinAmount() + " to Rs. " + service.getMaxAmount());
			valid = false;
		}
		error.setValid(valid);
		return error;
	}
}
