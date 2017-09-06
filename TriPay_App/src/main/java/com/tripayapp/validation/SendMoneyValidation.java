package com.tripayapp.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.model.SendMoneyBankDTO;
import com.tripayapp.model.SendMoneyMobileDTO;
import com.tripayapp.model.error.SendMoneyBankError;
import com.tripayapp.model.error.SendMoneyMobileError;
import com.tripayapp.repositories.PQServiceRepository;

public class SendMoneyValidation {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final PQServiceRepository pqServiceRepository;

	public SendMoneyValidation(PQServiceRepository pqServiceRepository) {
		this.pqServiceRepository = pqServiceRepository;
	}

	public SendMoneyMobileError checkMobileError(SendMoneyMobileDTO mobile, String username, String serviceCode) {
		SendMoneyMobileError error = new SendMoneyMobileError();
		boolean valid = true;
		if (mobile.getMobileNumber().equals(username)) {
			error.setMobileNumber("You cannot send money to your own mobile.....");
			valid = false;
		}
		

		PQService service = pqServiceRepository.findServiceByCode(serviceCode);
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(), mobile.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. " + service.getMaxAmount());
			valid = false;
		}

		error.setValid(valid);
		return error;
	}

	public SendMoneyBankError checkBankError(SendMoneyBankDTO bank, String serviceCode) {
		SendMoneyBankError error = new SendMoneyBankError();
		boolean valid = true;
		PQService service = pqServiceRepository.findServiceByCode(serviceCode);
		if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(), bank.getAmount())) {
			error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. " + service.getMaxAmount());
			valid = false;
		}
		error.setValid(valid);
		return error;
	}
}
