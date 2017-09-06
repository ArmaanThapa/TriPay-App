package com.tripayapp.model;

import java.util.List;

import com.tripayapp.entity.PQTransaction;

public class TransactionReportDTO {

	private String sessionId;
	private List<PQTransaction> transactions;

	public List<PQTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<PQTransaction> transactions) {
		this.transactions = transactions;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}