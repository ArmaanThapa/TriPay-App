package com.tripayapp.entity;

import javax.persistence.Entity;

import com.tripayapp.model.Status;

@Entity
public class PQOperator extends AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;

	private String name;
	private Status status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
