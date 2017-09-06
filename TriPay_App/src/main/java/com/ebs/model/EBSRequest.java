package com.ebs.model;

import org.json.JSONException;
import org.json.JSONObject;

public class EBSRequest {

	private String sessionId;

	private String channel;
	private String account_id;

	private String reference_no;

	private String amount;
	private String mode;

	private String currency;
	private String description;
	private String return_url;

	private String name;
	private String address;
	private String city;
	private String state;
	private String country;
	private String postal_code;
	private String phone;
	private String email;

	private String ship_name;
	private String ship_address;
	private String ship_city;
	private String ship_state;
	private String ship_country;
	private String ship_postal_code;
	private String ship_phone;

	private String secure_hash;
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("channel", channel);
			json.put("account_id", account_id);
			
			json.put("reference_no", reference_no);
			
			json.put("amount", amount);
			json.put("mode", mode);
			
			json.put("currency", currency);
			json.put("description", description);
			json.put("return_url", return_url);
			
			json.put("name", name);
			json.put("address", address);
			json.put("city", city);
			json.put("state", state);
			json.put("country", country);
			json.put("postal_code", postal_code);
			json.put("phone", phone);
			json.put("email", email);
			
			json.put("ship_name", ship_name);
			json.put("ship_address", ship_address);
			json.put("ship_city", ship_city);
			json.put("ship_state", ship_state);
			json.put("ship_country", ship_country);
			json.put("ship_postal_code", ship_postal_code);
			json.put("ship_phone", ship_phone);
			json.put("secure_hash", secure_hash);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSecure_hash() {
		return secure_hash;
	}

	public void setSecure_hash(String secure_hash) {
		this.secure_hash = secure_hash;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getReference_no() {
		return reference_no;
	}

	public void setReference_no(String reference_no) {
		this.reference_no = reference_no;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShip_name() {
		return ship_name;
	}

	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}

	public String getShip_address() {
		return ship_address;
	}

	public void setShip_address(String ship_address) {
		this.ship_address = ship_address;
	}

	public String getShip_city() {
		return ship_city;
	}

	public void setShip_city(String ship_city) {
		this.ship_city = ship_city;
	}

	public String getShip_state() {
		return ship_state;
	}

	public void setShip_state(String ship_state) {
		this.ship_state = ship_state;
	}

	public String getShip_country() {
		return ship_country;
	}

	public void setShips_country(String ship_country) {
		this.ship_country = ship_country;
	}

	public String getShip_postal_code() {
		return ship_postal_code;
	}

	public void setShip_postal_code(String ship_postal_code) {
		this.ship_postal_code = ship_postal_code;
	}

	public String getShip_phone() {
		return ship_phone;
	}

	public void setShip_phone(String ship_phone) {
		this.ship_phone = ship_phone;
	}

	public void setShip_country(String ship_country) {
		this.ship_country = ship_country;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
