package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class TelcoPlans extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	@OneToOne
	@JoinColumn
	private TelcoOperator operator;
	private String state;
	private String planName;
	private String amount;
	@Column(columnDefinition = "TEXT")
	private String description;
	@OneToOne
	@JoinColumn
	private TelcoCircle circle;
	private String validity;
	private String planType;
	private String operatorCode;
	private String smsDaakCode;

	public TelcoOperator getOperator() {
		return operator;
	}

	public void setOperator(TelcoOperator operator) {
		this.operator = operator;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TelcoCircle getCircle() {
		return circle;
	}

	public void setCircle(TelcoCircle circle) {
		this.circle = circle;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getSmsDaakCode() {
		return smsDaakCode;
	}

	public void setSmsDaakCode(String smsDaakCode) {
		this.smsDaakCode = smsDaakCode;
	}

}
