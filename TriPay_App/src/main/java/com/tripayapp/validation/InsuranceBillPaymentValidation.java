package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.InsuranceBillPaymentDTO;
import com.tripayapp.model.error.InsuranceBillPaymentError;
import com.tripayapp.repositories.PQServiceRepository;

public class InsuranceBillPaymentValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public InsuranceBillPaymentValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public InsuranceBillPaymentError checkError(InsuranceBillPaymentDTO insurance) {
		InsuranceBillPaymentError error = new InsuranceBillPaymentError();
		boolean valid = true;
//		if (CommonValidation.isNull(insurance.getAmount())) {
//			error.setAmount("Please enter amount  in the field");
//			valid = false;
//		}
//		if (CommonValidation.isNull(insurance.getPolicyDate())) {
//			error.setPolicyDate("Please select your policy date");
//			valid = false;
//		}
//		if (CommonValidation.isNull(insurance.getPolicyNumber())) {
//			error.setAmount("Please enter policy number");
//			valid = false;
//		}
//		if (CommonValidation.isNull(insurance.getServiceProvider())) {
//			error.setAmount("Please select Service Provider");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(insurance.getPolicyNumber())) {
//			error.setPolicyNumber("Policy number must be in numeric form");
//			valid = false;
//		}
//		if (!CommonValidation.isNumeric(insurance.getAmount())) {
//			error.setAmount("Amount must be in numeric form");
//			valid = false;
//		}

		PQService service = pqServiceRepository.findServiceByCode(insurance.getServiceProvider());
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(),
				insurance.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. "
					+ service.getMaxAmount());
			valid = false;
		}
		error.setValid(valid);
		return error;
	}
}
