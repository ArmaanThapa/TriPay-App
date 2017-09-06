package com.tripayapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class TelcoMap extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String number;
	
	@OneToOne(fetch = FetchType.EAGER)
	private TelcoOperator operator;
	
	@OneToOne(fetch = FetchType.EAGER)
	private TelcoCircle circle;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public TelcoOperator getOperator() {
		return operator;
	}

	public void setOperator(TelcoOperator operator) {
		this.operator = operator;
	}

	public TelcoCircle getCircle() {
		return circle;
	}

	public void setCircle(TelcoCircle circle) {
		this.circle = circle;
	}

}
