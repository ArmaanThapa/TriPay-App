package com.tripayapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PayAtStoreDTO {

	private String sessionId;
	private String serviceProvider;
	private String orderID;
	private String amount;
	private String message;

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("serviceProvider", serviceProvider);
			json.put("orderID", orderID);
			json.put("amount", amount);
			json.put("message", message);
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

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
