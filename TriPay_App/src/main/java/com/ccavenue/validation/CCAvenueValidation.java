package com.ccavenue.validation;

import com.ccavenue.model.CCAvenueRequest;
import com.ccavenue.model.error.CCAvenueRequestError;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.User;
import com.tripayapp.validation.CommonValidation;

public class CCAvenueValidation {

	private final IUserApi userApi;

	public CCAvenueValidation(IUserApi userApi) {
		this.userApi = userApi;
	}
	
	public CCAvenueRequestError validateCCAvenueRequest(CCAvenueRequest request) {
		CCAvenueRequestError error = new CCAvenueRequestError();
		boolean valid = true;
		if(CommonValidation.isNull(request.getAmount())) {
			error.setAmount("Please enter amount in field");
			valid = false;
		}
		if(!(CommonValidation.isValidLoadMoneyTransaction(request.getAmount()))) {
			error.setAmount("enter valid amount (1-10000)");
			valid = false;
		}
		User user = userApi.findByUserName(request.getCustomerIdentifier());
		if(user != null) {
			if(user.getEmailStatus().equals("Inactive")) {
				error.setCustomer_identifier("Please Verify your email before continue.....");
				valid = false;
			}
			if(user.getMobileStatus().equals("Inactive")) {
				error.setCustomer_identifier("Please verify your mobile number before continue.....");
				valid = false;
			}
		}
		error.setValid(valid);
		return error;
	}

}
