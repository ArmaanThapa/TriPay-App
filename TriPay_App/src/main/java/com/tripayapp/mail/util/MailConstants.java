package com.tripayapp.mail.util;

import java.util.Properties;

public class MailConstants {

//	public static final String SENDER_EMAIL = "carepayqwik@gmail.com";
//	public static final String PASSWORD = "Payqwik123";
//	public static final String USER_ID = "carepayqwik@gmail.com";
//	public static final String SMTP_AUTH = "true";
//	public static final String SMTP_STARTTLS_ENABLE = "true";
//	public static final boolean SMTP_AUTH_PLAIN_DISABLE = true;
//	public static final String SMTP_HOST = "smtp.gmail.com";
//	public static final String SMTP_SSL_TRUST = "smtp.gmail.com";
//	public static final String SMTP_PORT = "587";

//	public static final String CC_MAIL = "info@msewa.com";
//	public static final String SENDER_EMAIL = "vpayqwikcare@vijayabank.co.in";
//	public static final String PASSWORD = "vijaya@123";
//	public static final String USER_ID = "vpayqwikcare";
	
	
	public static final String CC_MAIL = "info@msewa.com";
	public static final String SENDER_EMAIL = "kamal@msewa.com";
	public static final String PASSWORD = "9066165729";
	public static final String USER_ID = "tripaycare";
	
//for no reply mail
	public static final String SENDER_EMAIL_NO_REPLY = "vpayqwikcare@vijayabank.co.in";
	public static final String USER_ID_NO_REPLY = "noreplyvpayqwikcare";


	public static final String SMTP_AUTH = "true";
	public static final String SMTP_STARTTLS_ENABLE = "true";
	public static final boolean SMTP_AUTH_PLAIN_DISABLE = true;
	public static final String SMTP_HOST = "172.16.7.206";
	public static final String SMTP_SSL_TRUST = "webmail.vijayabank.co.in";
	public static final String SMTP_PORT = "25";

	public static Properties getEmailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", MailConstants.SMTP_AUTH);
		props.put("mail.smtp.starttls.enable", MailConstants.SMTP_STARTTLS_ENABLE);
		props.put("mail.smtp.auth.plain.disable", MailConstants.SMTP_AUTH_PLAIN_DISABLE);
		props.put("mail.smtp.host", MailConstants.SMTP_HOST);
//		props.put("mail.smtp.ssl.trust", MailConstants.SMTP_SSL_TRUST);
		props.put("mail.smtp.port", MailConstants.SMTP_PORT);
		return props;
	}


}
