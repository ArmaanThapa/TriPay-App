package com.tripayapp.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.tripayapp.model.Status;





@Entity
public class TravelBusTransaction extends AbstractEntity<Long> {

	private static final long serialVersionUID=1L;
	
	private String amount;
	
	private String bookingRefNo;
	
	private String blockRefNo;
	
	private String description;
	
	@Column(nullable=false)
	private Date bookingDate;

	@OneToOne(fetch=FetchType.EAGER)
	private PQTransaction transaction;
	
	
	@OneToOne(fetch=FetchType.EAGER)
	private TravelBusDetail busDetail;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	@Column(unique=true)
	private String busTransactionRefNo;
	
	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getBookingRefNo() {
		return bookingRefNo;
	}


	public void setBookingRefNo(String bookingRefNo) {
		this.bookingRefNo = bookingRefNo;
	}


	public String getBlockRefNo() {
		return blockRefNo;
	}


	public void setBlockRefNo(String blockRefNo) {
		this.blockRefNo = blockRefNo;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public String getBusTransactionRefNo() {
		return busTransactionRefNo;
	}


	public void setBusTransactionRefNo(String busTransactionRefNo) {
		this.busTransactionRefNo = busTransactionRefNo;
	}


	public Date getBookingDate() {
		return bookingDate;
	}


	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}


	public PQTransaction getTransaction() {
		return transaction;
	}


	public void setTransaction(PQTransaction transaction) {
		this.transaction = transaction;
	}


	public TravelBusDetail getBusDetail() {
		return busDetail;
	}


	public void setBusDetail(TravelBusDetail busDetail) {
		this.busDetail = busDetail;
	}



	
	
	
}
