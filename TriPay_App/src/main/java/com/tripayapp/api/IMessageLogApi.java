package com.tripayapp.api;

import java.util.Date;
import java.util.List;

import com.tripayapp.entity.MessageLog;

public interface IMessageLogApi {

	List<MessageLog> getDailyMessageLogBetweeen(Date from, Date to);
}
