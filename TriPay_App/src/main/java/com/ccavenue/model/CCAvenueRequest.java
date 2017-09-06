package com.ccavenue.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CCAvenueRequest {

	private String tid;
	private String merchantId;
	private String orderId;
	private String currency;
	private String amount;
	private String redirectURL;
	private String cancelURL;
	private String language;

	// Billing Details
	private String billingName;
	private String billingAddress;
	private String billingCity;
	private String billingState;
	private String billingZip;
	private String billingCountry;
	private String billingTel;
	private String billingEmail;

	// Delivery Details
	private String deliveryName;
	private String deliveryAddress;
	private String deliveryCity;
	private String deliveryState;
	private String deliveryZip;
	private String deliveryCountry;
	private String deliveryTel;

	// Merchant Parameters
	private String merchantParam1;
	private String merchantParam2;
	private String merchantParam3;
	private String merchantParam4;
	private String merchantParam5;

	private String promoCode;

	// Customer Identifier for Saved User Cards
	private String customerIdentifier;

	private String encryptedResponse;
	
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getCancelURL() {
		return cancelURL;
	}

	public void setCancelURL(String cancelURL) {
		this.cancelURL = cancelURL;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingZip() {
		return billingZip;
	}

	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getBillingTel() {
		return billingTel;
	}

	public void setBillingTel(String billingTel) {
		this.billingTel = billingTel;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryCity() {
		return deliveryCity;
	}

	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getDeliveryZip() {
		return deliveryZip;
	}

	public void setDeliveryZip(String deliveryZip) {
		this.deliveryZip = deliveryZip;
	}

	public String getDeliveryCountry() {
		return deliveryCountry;
	}

	public void setDeliveryCountry(String deliveryCountry) {
		this.deliveryCountry = deliveryCountry;
	}

	public String getDeliveryTel() {
		return deliveryTel;
	}

	public void setDeliveryTel(String deliveryTel) {
		this.deliveryTel = deliveryTel;
	}

	public String getMerchantParam1() {
		return merchantParam1;
	}

	public void setMerchantParam1(String merchantParam1) {
		this.merchantParam1 = merchantParam1;
	}

	public String getMerchantParam2() {
		return merchantParam2;
	}

	public void setMerchantParam2(String merchantParam2) {
		this.merchantParam2 = merchantParam2;
	}

	public String getMerchantParam3() {
		return merchantParam3;
	}

	public void setMerchantParam3(String merchantParam3) {
		this.merchantParam3 = merchantParam3;
	}

	public String getMerchantParam4() {
		return merchantParam4;
	}

	public void setMerchantParam4(String merchantParam4) {
		this.merchantParam4 = merchantParam4;
	}

	public String getMerchantParam5() {
		return merchantParam5;
	}

	public void setMerchantParam5(String merchantParam5) {
		this.merchantParam5 = merchantParam5;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getCustomerIdentifier() {
		return customerIdentifier;
	}

	public void setCustomerIdentifier(String customerIdentifier) {
		this.customerIdentifier = customerIdentifier;
	}

	public String getEncryptedResponse() {
		return encryptedResponse;
	}

	public void setEncryptedResponse(String encryptedResponse) {
		this.encryptedResponse = encryptedResponse;
	}
	
	@Override
	public String toString() {
		return "MP1" + getMerchantParam1() 
		+ "MP2" + getMerchantParam2()
		+ "MP3" + getMerchantParam2()
		+ "MP4" + getMerchantParam2()
		+ "MP5" + getMerchantParam2()
		+ "DName" + getDeliveryName()
		+ "DAddress" + getDeliveryAddress()
		+ "DCity" + getDeliveryCity()
		+ "DState" + getDeliveryState()
		+ "DZip" + getDeliveryZip()
		+ "DCountry" + getDeliveryCountry()
		+ "DTel" + getDeliveryTel()
		+ "BName" + getBillingName()
		+ "BAddress" + getBillingAddress()
		+ "BCity" + getBillingCity()
		+ "BState" + getBillingState()
		+ "BZip" + getBillingZip()
		+ "BCountry" + getBillingCountry()
		+ "BTel" + getBillingTel()
		+ "BEmail" + getBillingEmail()
		+ "PCode" + getPromoCode()
		+ "CIden" + getCustomerIdentifier()
		+ "TID" + getTid()
		+ "MID" + getMerchantId()
		+ "OID" + getOrderId()
		+ "Cur" + getCurrency()
		+ "Amt" + getAmount()
		+ "RURL" + getRedirectURL()
		+ "CURL" + getCancelURL()
		+ "Lang" + getLanguage();
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("tid", tid);
			json.put("merchantId", merchantId);
			json.put("orderId", orderId);
			json.put("currency", currency);
			json.put("amount", amount);
			json.put("redirectURL", redirectURL);
			json.put("cancelURL", cancelURL);
			json.put("language", language);
			
			json.put("billingName", billingName);
			json.put("billingAddress", billingAddress);
			json.put("billingCity", billingCity);
			json.put("billingState", billingState);
			json.put("billingZip", billingZip);
			json.put("billingCountry", billingCountry);
			json.put("billingTel", billingTel);
			json.put("billingEmail", billingEmail);
			
			json.put("deliveryName", deliveryName);
			json.put("deliveryAddress", deliveryAddress);
			json.put("deliveryCity", deliveryCity);
			json.put("deliveryState", deliveryState);
			json.put("deliveryZip", deliveryZip);
			json.put("deliveryCountry", deliveryCountry);
			json.put("deliveryTel", deliveryTel);
			
			json.put("merchantParam1", merchantParam1);
			json.put("merchantParam2", merchantParam2);
			json.put("merchantParam3", merchantParam3);
			json.put("merchantParam4", merchantParam4);
			json.put("merchantParam5", merchantParam5);
			
			json.put("promoCode", promoCode);
			json.put("customerIdentifier", customerIdentifier);
			json.put("encryptedResponse", encryptedResponse);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

}
