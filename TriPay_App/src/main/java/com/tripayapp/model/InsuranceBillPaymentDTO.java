package com.tripayapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class InsuranceBillPaymentDTO {

	private String sessionId;

	private String serviceProvider;

	private String policyNumber;

	private String policyDate;

	private String amount;
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("serviceProvider", serviceProvider);
			json.put("policyNumber", policyNumber);
			json.put("policyDate", policyDate);
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

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyDate() {
		return policyDate;
	}

	public void setPolicyDate(String policyDate) {
		this.policyDate = policyDate;
	}

	public String getAmount() {
		return amount;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
