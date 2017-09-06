package com.tripayapp.cronjob;

import com.tripayapp.api.IUserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionOperations;

import com.tripayapp.api.ITransactionApi;

public class AutoReverse {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final ITransactionApi transactionApi;
	private final TransactionOperations transactionOperations;

	public AutoReverse(IUserApi userApi,ITransactionApi transactionApi, TransactionOperations transactionOperations) {
		this.userApi = userApi;
		this.transactionApi = transactionApi;
		this.transactionOperations = transactionOperations;
	}

	public void checkAccountForReverse() {
//		logger.info("cron");
//	    transactionApi.revertSendMoneyOperations();
	}

	public void unblockUser(){
		logger.info("cron for unblock user");
		userApi.revertAuthority();
	}

}
