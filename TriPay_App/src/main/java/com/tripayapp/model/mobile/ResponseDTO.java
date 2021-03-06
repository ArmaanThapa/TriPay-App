package com.tripayapp.model.mobile;

public class ResponseDTO{

	private String status;
	private String code;
	private String message;
	private double balance;
	private Object details;

	public String getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status.getKey();
		this.code = status.getValue();
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
