package com.tripayapp.api.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.tripayapp.api.IEmailLogApi;
import com.tripayapp.entity.EmailLog;
import com.tripayapp.repositories.EmailLogRepository;

public class EmailLogApi implements IEmailLogApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MessageSource messageSource;
	
	private final EmailLogRepository emailLogRepository;

	public EmailLogApi(EmailLogRepository emailLogRepository) {
		this.emailLogRepository = emailLogRepository;
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public List<EmailLog> getDailyEmailLogBetweeen(Date from, Date to) {
		return emailLogRepository.getDailyEmailLogBetween(from, to);
	}

}
