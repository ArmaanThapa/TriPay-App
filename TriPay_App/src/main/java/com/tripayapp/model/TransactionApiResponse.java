package com.tripayapp.model;

import com.tripayapp.entity.PQTransaction;

public class TransactionApiResponse {

	private String sessionId;
	private Status status;
	private PQTransaction debit;
	private PQTransaction credit;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public PQTransaction getDebit() {
		return debit;
	}

	public void setDebit(PQTransaction debit) {
		this.debit = debit;
	}

	public PQTransaction getCredit() {
		return credit;
	}

	public void setCredit(PQTransaction credit) {
		this.credit = credit;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
