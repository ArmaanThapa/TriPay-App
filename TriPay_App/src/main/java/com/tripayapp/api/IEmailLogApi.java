package com.tripayapp.api;

import java.util.Date;
import java.util.List;

import com.tripayapp.entity.EmailLog;

public interface IEmailLogApi {

	List<EmailLog> getDailyEmailLogBetweeen(Date from, Date to);
	
}
