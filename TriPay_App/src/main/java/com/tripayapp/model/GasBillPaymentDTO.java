package com.tripayapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class GasBillPaymentDTO {

	private String sessionId;

	private String serviceProvider;

	private String accountNumber;

	private String amount;
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("serviceProvider", serviceProvider);
			json.put("accountNumber", accountNumber);
			json.put("amount", amount);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
