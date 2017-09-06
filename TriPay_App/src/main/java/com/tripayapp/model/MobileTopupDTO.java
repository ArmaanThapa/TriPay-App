package com.tripayapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileTopupDTO {

	private String sessionId;
	private TopupType topupType;
	private String serviceProvider;
	private String mobileNo;
	private String area;
	private String amount;
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("serviceProvider", serviceProvider);
			json.put("mobileNo", mobileNo);
			json.put("area", area);
			json.put("amount", amount);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public TopupType getTopupType() {
		return topupType;
	}

	public void setTopupType(TopupType topupType) {
		this.topupType = topupType;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
