package com.tripayapp.api.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.tripayapp.api.IMessageLogApi;
import com.tripayapp.entity.MessageLog;
import com.tripayapp.repositories.MessageLogRepository;

public class MessageLogApi implements IMessageLogApi, MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final MessageLogRepository messageLogRepository;

	public MessageLogApi(MessageLogRepository messageLogRepository) {
		this.messageLogRepository = messageLogRepository;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public List<MessageLog> getDailyMessageLogBetweeen(Date from, Date to) {
		return messageLogRepository.getDailyMessageLogBetween(from, to);
	}

}
