package com.tripayapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class SendMoneyBankDTO {

	private String sessionId;
	private String bankCode;
	private String ifscCode;
	private String username;
	private long accountNumber;
	private String amount;
	private String accountName;
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("bankCode", getBankCode());
			json.put("ifscCode", getIfscCode());
			json.put("username", getUsername());
			json.put("amount", getAmount());
			json.put("accountNumber", getAccountNumber());
			json.put("accountName", getAccountName());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}