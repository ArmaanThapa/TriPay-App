package com.tripayapp.validation;

import com.tripayapp.model.error.LoadMoneyError;

public class LoadMoneyValidation {

	public LoadMoneyError checkError(double amount) {
		LoadMoneyError error = new LoadMoneyError();
		boolean valid = true;
//		if (!CommonValidation.isGreaterthan10(amount)) {
//			error.setAmount("Amount must be greater than 10");
//			valid = false;
//		}
		error.setValid(valid);
		return error;
	}
}
