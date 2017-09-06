package com.instantpay.util;

public class InstantPayConstants {

	private static final String URL = "http://172.16.7.29/InstantPay/"; // VPAYQWIK
//	private static final String URL = "http://172.16.7.29:8089/InstantPay/"; // VPAYQWIK

//	private static final String URL = "http://192.168.1.17:8080/InstantPay/"; // IMPORTANT URL FOR BANK PRODUCTION USE
//	private static final String URL = "http://66.207.206.54:8034/InstantPay/";
	
	public static final String URL_TRANSACTION = URL + "Transaction/Process";
	public static final String URL_BALANCE = URL + "Balance/Process";
	public static final String URL_STATUS = URL + "StatusCheck/Process";
	public static final String URL_SERVICE = URL + "Services/Process";
	public static final String URL_VALIDATION = URL + "Validation/Process";

	// 66.207.206.54 - MSEWA
//	public static final String TOKEN = "936267b2a4cc1fef34d7396376ca21c0";
	// 172.16.3.10 - VIJAYA BANK
//	 public static final String TOKEN = "67af5efa626db09dc6771c596d8428b4";
	// 172.16.7.29 - VIJAYA BANK WEB
//	 public static final String TOKEN = "5cb1d335e315f5bd99754e50ea2b5d59";
	 // 210.212.204.39 - Vijaya Bank (USE THIS PUBLIC IP TOKEN)
	
	public static final String TOKEN = "7563bd75da0a1d9b38676c81bdd76963";
	public static final String REQUEST_FORMAT = "json";
	public static final String AGENT_ID = "565Y10837"; // MSEWA Dealer code
	public static final String OUTLET_ID = "10019339"; // MSEWA Outlet ID for 66.207.206.54
	public static final String API_KEY_TOKEN = "token";
	public static final String API_KEY_MODE = "mode";
	public static final String API_KEY_SPKEY = "spKey";
	public static final String API_KEY_AGENTID = "agentId";
	public static final String API_KEY_ACCOUNT = "account";
	public static final String API_KEY_AMOUNT = "amount";
	public static final String API_KEY_OPTIONAL1 = "optional1";
	public static final String API_KEY_OPTIONAL2 = "optional2";
	public static final String API_KEY_OPTIONAL3 = "optional3";
	public static final String API_KEY_OPTIONAL4 = "optional4";
	public static final String API_KEY_OPTIONAL5 = "optional5";
	public static final String API_KEY_FORMAT = "format";
	public static final String API_KEY_TYPE = "type";
	public static final String USERNAME = "instantpay@tripay.in";

}
