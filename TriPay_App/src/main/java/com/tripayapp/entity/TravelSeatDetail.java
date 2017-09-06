package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;





@Entity
public class TravelSeatDetail extends AbstractEntity<Long> {

	private static final long serialVersionUID=1L;
	
	@Column(nullable=false)
	private String seatNo;
	
	@Column(nullable=false)
	private String fare;
	
	private String noOfSeats;
	
	private String cancelPolicy;

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
	}

	public String getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(String noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public String getCancelPolicy() {
		return cancelPolicy;
	}

	public void setCancelPolicy(String cancelPolicy) {
		this.cancelPolicy = cancelPolicy;
	}
	
	
	
	
}
