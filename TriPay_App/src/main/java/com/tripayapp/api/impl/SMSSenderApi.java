package com.tripayapp.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.tripayapp.api.ISMSSenderApi;
import com.tripayapp.entity.MessageLog;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserDetail;
import com.tripayapp.repositories.MessageLogRepository;
import com.tripayapp.sms.util.SMSAccount;
import com.tripayapp.sms.util.SMSTemplate;
import com.tripayapp.util.TriPayUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class SMSSenderApi implements ISMSSenderApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;
	//	 private static String SMSURL = "http://66.207.206.54:8035/SendSMS/"; // FGM TEST Server
     private static String SMSURL = "http://172.16.7.29/SendSMS/"; // Vijaya Bank

	private MessageLogRepository messageLogRepository;
	private VelocityEngine velocityEngine;

	public void setSmsSender(VelocityEngine velocityEngine, MessageLogRepository messageLogRepository) {
		this.velocityEngine = velocityEngine;
		this.messageLogRepository = messageLogRepository;
	}

	public void setMessageLogRepository(MessageLogRepository messageLogRepository) {
		this.messageLogRepository = messageLogRepository;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void sendUserSMS(SMSAccount smsAccount, String smsTemplate, User user,String additionalInfo) {
		Map model = new HashMap();
		model.put("user", user);
		model.put("info", additionalInfo);
		String smsMessage = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				TriPayUtil.SMS_TEMPLATE + smsTemplate, model);
		sendSMS(smsAccount, user, smsMessage, smsTemplate);
	}

	@Override
	public void sendMerchantSMS(SMSAccount smsAccount, String smsTemplate, User user, String additionalInfo) {
		Map model = new HashMap();
		model.put("user", user);
		model.put("info",additionalInfo);
		String smsMessage = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				TriPayUtil.SMS_TEMPLATE + smsTemplate, model);
		sendSMS(smsAccount, user, smsMessage, smsTemplate);
	}

	@Override
	public void sendTransactionSMS(SMSAccount smsAccount, String smsTemplate, User user, PQTransaction transaction,String additionalInfo) {
		Map model = new HashMap();
		model.put("user", user);
		model.put("transaction", transaction);
		model.put("info",additionalInfo);
//		System.out.println("transaction.getServiceType() "+transaction.getService());
//		System.out.println("transaction.getServiceType().getCode() "+transaction.getService().getCode());

		if (transaction.getService() != null && transaction.getService().getCode() != null) {
			if (transaction.getService().getCode().equals("SMM") || transaction.getService().getCode().equals("SMU")) {
                    System.out.println("Inside Send Money Transaction Split");
					String[] parts = transaction.getDescription().split(" ");
                    for(String temp : parts){
                        System.out.println(temp);
                    }
					model.put("sender", parts[4]);
			}
		}
		String smsMessage = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				TriPayUtil.SMS_TEMPLATE + smsTemplate, model);
		sendSMS(smsAccount, user, smsMessage, smsTemplate);
	}

	private void sendSMS(final SMSAccount smsAccount, final User destination, final String smsMessage,
			final String smsTemplate) {
		try {
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						Client client = Client.create();
						WebResource webResource = client.resource(SMSURL);
						MultivaluedMapImpl smsData = new MultivaluedMapImpl();
						smsData.add("username", smsAccount.getUsername());
						smsData.add("password", smsAccount.getPassword());
						smsData.add("type", smsAccount.getType());
						smsData.add("dlr", smsAccount.getDlr());
						smsData.add("destination", destination.getUserDetail().getContactNo());
						smsData.add("source", smsAccount.getSource());
						smsData.add("message", smsMessage);
						ClientResponse response = webResource.accept("application/json").post(ClientResponse.class,smsData);
						String strResponse = response.getEntity(String.class);

						if (response.getStatus() == 200) {
							saveLog(destination, smsTemplate, smsMessage, strResponse);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private void sendSMSGeneric(final SMSAccount smsAccount, final User destination, final String mobileNumber,final String smsMessage,
						 final String smsTemplate) {
		try {
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						Client client = Client.create();
						WebResource webResource = client.resource(SMSURL);
						MultivaluedMapImpl smsData = new MultivaluedMapImpl();
						smsData.add("username", smsAccount.getUsername());
						smsData.add("password", smsAccount.getPassword());
						smsData.add("type", smsAccount.getType());
						smsData.add("dlr", smsAccount.getDlr());
						smsData.add("destination", mobileNumber);
						smsData.add("source", smsAccount.getSource());
						smsData.add("message", smsMessage);
						ClientResponse response = webResource.accept("application/json").post(ClientResponse.class,
								smsData);
						String strResponse = response.getEntity(String.class);
						if (response.getStatus() == 200) {
							saveLog(destination, smsTemplate, smsMessage, strResponse);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void saveLog(User user, String smsTemplate, String smsMessage, String smsResponse) {
		MessageLog mgslog = new MessageLog();
		mgslog.setExecutionTime(new Date());
		mgslog.setDestination(user.getUsername());
		mgslog.setMessage(smsMessage);
		mgslog.setTemplate(smsTemplate);
		mgslog.setResponse(smsResponse);
		messageLogRepository.save(mgslog);
	}

	@Override
	public void promotionalEmails(String number, String message) {
		Map model = new HashMap();
		model.put("model", message);
		User user = new User();
		user.setUsername(number);
		UserDetail userDetaills = new UserDetail();
		userDetaills.setContactNo(number);
		user.setUserDetail(userDetaills);
		String smsMessage = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				TriPayUtil.SMS_TEMPLATE + SMSTemplate.PROMOTIONAL_SMS, model);
		sendSMS(SMSAccount.PAYQWIK_PROMOTIONAL, user, smsMessage, SMSTemplate.PROMOTIONAL_SMS);
	}

	@Override
	public void sendKYCSMS(SMSAccount smsAccount, String smsTemplate,User user,String mobileNumber, String additionalInfo) {
		Map model = new HashMap();
		model.put("user", user);
		model.put("info",additionalInfo);
		String smsMessage = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				TriPayUtil.SMS_TEMPLATE + smsTemplate, model);
		sendSMSGeneric(smsAccount, user, mobileNumber,smsMessage, smsTemplate);
	}

}
