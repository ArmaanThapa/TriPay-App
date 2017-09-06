package com.tripayapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class PQCommission extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	private double minAmount;
	private double maxAmount;
	private boolean fixed;
	private double value;
	private String type;
	
	private String identifier;

	@OneToOne(fetch = FetchType.EAGER)
	private PQService service;

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PQService getService() {
		return service;
	}

	public void setService(PQService service) {
		this.service = service;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
