package com.tripayapp.entity;

import javax.persistence.Entity;

@Entity
public class PQAccountType extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String code;
	private double monthlyLimit;
	private double dailyLimit;
	private double balanceLimit;
	private int transactionLimit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(double monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}

	public double getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public double getBalanceLimit() {
		return balanceLimit;
	}

	public void setBalanceLimit(double balanceLimit) {
		this.balanceLimit = balanceLimit;
	}

	public int getTransactionLimit() {
		return transactionLimit;
	}

	public void setTransactionLimit(int transactionLimit) {
		this.transactionLimit = transactionLimit;
	}

}
