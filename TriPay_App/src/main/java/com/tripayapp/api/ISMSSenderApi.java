package com.tripayapp.api;

import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.sms.util.SMSAccount;

public interface ISMSSenderApi {

	void sendUserSMS(SMSAccount smsAccount, String smsTemplate, User user,String additionalInfo);

	void sendMerchantSMS(SMSAccount smsAccount, String smsTemplate, User user,String additionalInfo);

	void sendTransactionSMS(SMSAccount smsAccount, String smsTemplate, User user, PQTransaction transaction,String additionalInfo);

	void promotionalEmails(String number, String message);

	void sendKYCSMS(SMSAccount smsAccount, String smsTemplate,User user,String mobileNumber,String additionalInfo);

}
