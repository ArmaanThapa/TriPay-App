package com.tripayapp.model.error;

public class DTHBillPaymentError {

	private boolean valid;

	private String serviceProvider;

	private String dthNo;

	private String amount;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getDthNo() {
		return dthNo;
	}

	public void setDthNo(String dthNo) {
		this.dthNo = dthNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
