package com.tripayapp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.tripayapp.model.Status;

@Entity
public class PQService extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String name;
	private String code;
	private String operatorCode;
	private String description;
	private double minAmount;
	private double maxAmount;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	private PQServiceType serviceType;

	@OneToOne(fetch = FetchType.EAGER)
	private PQOperator operator;

	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REMOVE)
	@NotFound(action = NotFoundAction.IGNORE)
	private ClientDetail clientDetailID;
	

	public ClientDetail getClientDetailID() {
		return clientDetailID;
	}

	public void setClientDetailID(ClientDetail clientDetailID) {
		this.clientDetailID = clientDetailID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public PQServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(PQServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public PQOperator getOperator() {
		return operator;
	}

	public void setOperator(PQOperator operator) {
		this.operator = operator;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
}
