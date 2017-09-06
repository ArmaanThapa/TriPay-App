package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.tripayapp.model.Status;
import com.tripayapp.model.TransactionType;

@Entity
public class PQTransaction extends AbstractEntity<Long> {

	private static final long serialVersionUID = 7184799860853214548L;

	@ManyToOne(optional = false)
	private PQAccountDetail account;

	private double amount;

	private boolean debit;

	@Column(nullable = true)
	private boolean favourite;

	private double currentBalance;

	@Column(nullable = true)
	private String description;

	private TransactionType transactionType = TransactionType.DEFAULT;

	@OneToOne(fetch = FetchType.EAGER)
	private PQService service;

	@Column(unique = true)
	private String transactionRefNo;

	@Column(nullable = true)
	private String OTP;

	private String commissionIdentifier;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Lob
	private String request;

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public boolean isDebit() {
		return debit;
	}

	public void setDebit(boolean debit) {
		this.debit = debit;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public PQAccountDetail getAccount() {
		return account;
	}

	public void setAccount(PQAccountDetail account) {
		this.account = account;
	}

	public PQService getService() {
		return service;
	}

	public void setService(PQService service) {
		this.service = service;
	}

	public String getTransactionRefNo() {
		return transactionRefNo;
	}

	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}

	public String getOTP() {
		return OTP;
	}

	public void setOTP(String oTP) {
		this.OTP = oTP;
	}

	public String getCommissionIdentifier() {
		return commissionIdentifier;
	}

	public void setCommissionIdentifier(String commissionIdentifier) {
		this.commissionIdentifier = commissionIdentifier;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return ""+this.getCreated();
	}


	
}
