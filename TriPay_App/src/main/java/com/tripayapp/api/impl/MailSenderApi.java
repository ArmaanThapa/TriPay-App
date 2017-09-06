package com.tripayapp.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.instantpay.util.InstantPayConstants;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.tripayapp.api.IMailSenderApi;
import com.tripayapp.entity.EmailLog;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.mail.util.MailConstants;
import com.tripayapp.model.Status;
import com.tripayapp.repositories.EmailLogRepository;
import com.tripayapp.util.TriPayUtil;

public class MailSenderApi implements IMailSenderApi, MessageSourceAware {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;
	
	private JavaMailSender mailSender;
	private VelocityEngine velocityEngine;
	private EmailLogRepository emailLogRepository;

	public void setMailSender(JavaMailSender mailSender,
			VelocityEngine velocityEngine, EmailLogRepository emailLogRepository) {
		this.mailSender = mailSender;
		this.velocityEngine = velocityEngine;
		this.emailLogRepository = emailLogRepository;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setEmailLogRepository(EmailLogRepository emailLogRepository) {
		this.emailLogRepository = emailLogRepository;
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void sendMail(final String subject, final String mailTemplate,
			final User user, final String additionalInfo) throws MailSendException {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(user.getUserDetail().getEmail());
				message.setFrom("TriPay");
				message.setSubject(subject);
				Map model = new HashMap();
				model.put("user", user);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, TriPayUtil.MAIL_TEMPLATE
								+ mailTemplate, model);
				message.setText(text, true);
			}
		};
		sendAsync(this.mailSender, preparator);
		saveLog(subject, mailTemplate, user);
	}

	@Override
	public void sendChangePasswordMail(final String subject,
			final String mailTemplate, final User user,final String additionalInfo) throws MailSendException {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(user.getUserDetail().getEmail());
				message.setFrom("TriPay");
				message.setSubject(subject);
				Map model = new HashMap();
				model.put("user", user);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, TriPayUtil.MAIL_TEMPLATE
								+ mailTemplate, model);
				message.setText(text, true);
			}
		};
		sendAsync(this.mailSender, preparator);
		saveLog(subject, mailTemplate, user);
	}

	@Override
	public void sendTransactionMail(final String subject,
			final String mailTemplate, final User user,
			final PQTransaction transaction,final String additionalInfo) throws MailSendException {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(user.getUserDetail().getEmail());
				message.setFrom("TriPay");
				message.setSubject(subject);
				Map model = new HashMap();
				model.put("user", user);
				model.put("transaction", transaction);
				model.put("info",additionalInfo);
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, TriPayUtil.MAIL_TEMPLATE
								+ mailTemplate, model);
				message.setText(text, true);
			}
		};
		sendAsync(this.mailSender, preparator);
		saveLog(subject, mailTemplate, user);
	}
	
	@Override
	public void saveLog(String subject, String mailTemplate, User user) {
		EmailLog email = new EmailLog();
		email.setDestination(user.getUserDetail().getEmail());
		email.setExcutionTime(new Date());
		email.setMailTemplate(mailTemplate);
		email.setResponse(null);
		email.setStatus(Status.Inactive);
		emailLogRepository.save(email);
	}
	
	@Override
	public void sendAsync(final JavaMailSender mailSender, final MimeMessagePreparator preparator) {
		try {
			Thread t = new Thread(new Runnable() {
				public void run() {
					mailSender.send(preparator);
				}
			});
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}