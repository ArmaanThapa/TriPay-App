package com.tripayapp.sms.util;

public enum SMSAccount {
	
	PAYQWIK_OTP("msewaOTP", "Hnfk5qzx"), 
	PAYQWIK_TRANSACTIONAL("msewaTran", "oT2ClFs2"), 
	PAYQWIK_PROMOTIONAL("msewaScrub", "uuO2Qtgz"); 

	private final String type = "0";
	private final String dlr = "1";
	private final String source = "VPYQWK";
	
	private final String username;
	private final String password;

	SMSAccount(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public String getDlr() {
		return dlr;
	}

	public String getSource() {
		return source;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}