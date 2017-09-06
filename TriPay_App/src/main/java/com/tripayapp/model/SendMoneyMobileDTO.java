package com.tripayapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SendMoneyMobileDTO {

	private String sessionId;
	private String mobileNumber;
	private String amount;
	private String message;

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("mobileNumber", mobileNumber);
			json.put("amount", amount);
			json.put("message", message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
