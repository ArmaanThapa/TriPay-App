package com.tripayapp.model;

public class PromoTransactionDTO {
	private String amount;
	private String serviceCode;
	private String transactionDate;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "PromoTransactionDTO{" +
				"amount='" + amount + '\'' +
				", serviceCode='" + serviceCode + '\'' +
				", transactionDate='" + transactionDate + '\'' +
				'}';
	}
}
