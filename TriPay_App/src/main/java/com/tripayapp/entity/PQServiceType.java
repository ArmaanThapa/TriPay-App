package com.tripayapp.entity;

import javax.persistence.Entity;

@Entity
public class PQServiceType extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String name;
//	private String operator;
	private String description;
//	private String type;
//	private String code;
//	private double minAmount;
//	private double maxAmount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public String getOperator() {
//		return operator;
//	}
//
//	public void setOperator(String operator) {
//		this.operator = operator;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public double getMinAmount() {
//		return minAmount;
//	}
//
//	public void setMinAmount(double minAmount) {
//		this.minAmount = minAmount;
//	}
//
//	public double getMaxAmount() {
//		return maxAmount;
//	}
//
//	public void setMaxAmount(double maxAmount) {
//		this.maxAmount = maxAmount;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

}
