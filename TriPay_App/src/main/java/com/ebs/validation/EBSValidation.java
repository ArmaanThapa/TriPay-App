package com.ebs.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ebs.model.EBSRequest;
import com.ebs.model.error.EBSRequestError;
import com.tripayapp.entity.PQService;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.validation.CommonValidation;

public class EBSValidation {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final PQServiceRepository pqServiceRepository;

	public EBSValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public EBSRequestError validateRequest(EBSRequest request, String serviceCode) {
		EBSRequestError error = new EBSRequestError();
		boolean valid = true;
		  if (CommonValidation.isNull(request.getAmount())) {
			valid = false;
			error.setAmount("please enter amount");
		}else if (CommonValidation.isNull(request.getName())) {
			valid = false;
			error.setName("please enter name");
		} else if (CommonValidation.isNull(request.getPhone())) {
			valid = false;
			error.setPhone("please enter phone no.");
		} else if (CommonValidation.isNull(request.getEmail())) {
			valid = false;
			error.setEmail("please enter email");
		}

		PQService service = pqServiceRepository.findServiceByCode(serviceCode);
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(),
				request.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. "
					+ service.getMaxAmount());
			valid = false;
		}

		error.setValid(valid);
		return error;
	}

}
