package com.instantpay.model.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.instantpay.model.Balance;
import com.instantpay.model.Validation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponse {

	/**
	 * 
	 * Response ::
	 * {
		"Wallet": "21829.94"
	   }
	 */
	private boolean success;
	private Validation validation;
	private Balance balance;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public Validation getValidation() {
		return validation;
	}

	public void setValidation(Validation validation) {
		this.validation = validation;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	
	

}
