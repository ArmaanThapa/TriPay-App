package com.tripayapp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class PQAccountDetail extends AbstractEntity<Long> {

	private static final long serialVersionUID = -9163185658863000713L;

	private double balance;
	
	@Column(unique = true, nullable = false)
	private long accountNumber;

	@Column(columnDefinition = "bigint(20) default 0")
	private long points;

	@Column
	private String vijayaAccountNumber;

	private boolean isVBankCustomer;

	@OneToOne(fetch = FetchType.EAGER)
	private VBankAccountDetail vBankAccount;

	@OneToOne(fetch = FetchType.EAGER)
	private PQAccountType accountType;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
	private List<PQTransaction> transactions;

	private String branchCode;

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getVijayaAccountNumber() {
		return vijayaAccountNumber;
	}

	public void setVijayaAccountNumber(String vijayaAccountNumber) {
		this.vijayaAccountNumber = vijayaAccountNumber;
	}



	public boolean isVBankCustomer() {
		return isVBankCustomer;
	}

	public void setVBankCustomer(boolean VBankCustomer) {
		isVBankCustomer = VBankCustomer;
	}

	public VBankAccountDetail getvBankAccount() {
		return vBankAccount;
	}

	public void setvBankAccount(VBankAccountDetail vBankAccount) {
		this.vBankAccount = vBankAccount;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	@JsonIgnore
	public List<PQTransaction> getTransactions() {
		return transactions;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public void setTransactions(List<PQTransaction> transactions) {
		this.transactions = transactions;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public PQAccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(PQAccountType accountType) {
		this.accountType = accountType;
	}

}
