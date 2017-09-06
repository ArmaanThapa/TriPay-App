package com.tripayapp.api;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;

public interface IMailSenderApi {

//	void sendMail(final String subject, final String mailTemplate, final User user,String additionalInfo) throws MailSendException;
//
//	void sendNoReplyMail(final String subject, final String mailTemplate, final User user,String additionalInfo) throws MailSendException;
//
//	void sendChangePasswordMail(String subject, String mailTemplate, User user,String additionalInfo) throws MailSendException;
//	
//	void sendTransactionMail(final String subject, final String mailTemplate, final User user,
//			final PQTransaction transaction,String additionalInfo,String ccEmail) throws MailSendException;
//	
//	void saveLog(String subject, String mailTemplate, User user) throws MailSendException;
//	
//	void sendAsync(JavaMailSender mailSender, MimeMessagePreparator preparator);;
//	
//	void sendVijayaBankEmail(String subject, String mailTemplate, User user, PQTransaction transaction,String additionalInfo,String ccEmail) throws MailSendException;
//
//	void sendVijayaBankNoReplyEmail(String subject, String mailTemplate, User user, PQTransaction transaction,String additionalInfo) throws MailSendException;
//
//	void sendAsyncVijayaEmail(MimeMessage message);
//
//	void sendAsyncVijayaNoReplyEmail(MimeMessage message);
	
	
	void sendMail(String subject, String mailTemplate, User user,String additionalInfo) throws MailSendException;

	void sendChangePasswordMail(String subject, String mailTemplate, User user, String additionalInfo) throws MailSendException;
	
	void sendTransactionMail(String subject, String mailTemplate, User user, PQTransaction transaction, String additionalInfo) throws MailSendException;
	
	void saveLog(String subject, String mailTemplate, User user) throws MailSendException;
	
	void sendAsync(JavaMailSender mailSender, MimeMessagePreparator preparator);
	

}
