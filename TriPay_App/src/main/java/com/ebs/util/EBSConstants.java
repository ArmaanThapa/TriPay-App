package com.ebs.util;

public class EBSConstants {

	public static final String URL_INIT_TRANSACTION = "https://secure.ebs.in/pg/ma/payment/request";
	
	/* ===============KEYS FOR EBS GATEWAY===================================*/
	
	public static final String KEY_CHANNEL="channel";
	public static final String KEY_ACCOUNTID="account_id";
	public static final String KEY_REFERENCENO="reference_no";
	public static final String KEY_AMOUNT="amount";
	public static final String KEY_MODE="mode";
	public static final String KEY_CURRENCY="currency";
	public static final String KEY_DESCRIPTION="description";
	public static final String KEY_RETURN_URL="return_url";
	public static final String KEY_NAME="name";
	public static final String KEY_ADDRESS="address";
	public static final String KEY_CITY="city";
	public static final String KEY_STATE="state";
	public static final String KEY_COUNTRY="country";
	public static final String KEY_POSTAL_CODE="postal_code";
	public static final String KEY_PHONE="phone";
	public static final String KEY_EMAIL="email";
	public static final String KEY_SHIP_NAME="ship_name";
	public static final String KEY_SHIP_ADDRESS="ship_address";
	public static final String KEY_SHIP_STATE="ship_state";
	public static final String KEY_SHIP_CITY="ship_city";
	public static final String KEY_SHIP_COUNTRY="ship_country";
	public static final String KEY_SHIP_POSTAL_CODE="ship_postal_code";
	public static final String KEY_SHIP_PHONE="ship_phone";

	/*=================DEFAULT VALUES FOR KEYS==================================================*/
	
	public static final String CHANNEL_STANDARD="0";
	public static final String CHANNEL_DIRECT="2";
	public static final String MODE_TEST="TEST";
	public static final String MODE_LIVE="LIVE";
	public static final String CURRENCY="INR";
	public static final String DESCRIPTION = "Loading Money in VPayQwik Wallet of User";
//	public static final String RETURN_URL="https://www.vpayqwik.com/User/LoadMoney/Redirect";
	public static final String RETURN_URL="http://66.207.206.54:8034/User/LoadMoney/Redirect";
	public static final String PAGE_ID="4837";
	public static final String PAYMENT_MODE_CREDIT_CARD="1";
	public static final String PAYMENT_MODE_DEBIT_CARD="2";
	public static final String PAYMENT_MODE_NET_BANKING="3";
	public static final String PAYMENT_MODE_CASH_CARD="4";
	public static final String PAYMENT_MODE_CREDIT_CARD_EMI="5";
	public static final String ALGO_MD5="MD5";
	public static final String ALGO_SHA1="SHA1";
	public static final String ALGO_SHA512="SHA512";
	public static final String ACCOUNT_ID= "20696";
	public static final String SECRET_KEY = "6496e4db9ebf824ffe2269afee259447";
	
	/*================================SHIPPING CONSTANTS======================================*/
	
	public static final String SHIP_NAME="MSEWA";
	public static final String SHIP_ADDRESS="#106,4th Cross 2nd Block Koramangala";
	public static final String SHIP_STATE="Karnataka";
	public static final String SHIP_CITY="Bengaluru";
	public static final String SHIP_COUNTRY="IND";
	public static final String SHIP_POSTAL_CODE="560034";
	public static final String SHIP_PHONE="7022620747";
	
}
