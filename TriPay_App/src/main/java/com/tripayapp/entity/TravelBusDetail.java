package com.tripayapp.entity;





import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;



@Entity
public class TravelBusDetail extends AbstractEntity<Long> {

	private static final long serialVersionUID=1L;
	
	@Column(nullable=false)
	private String tripId;
	
	@Column(nullable=false)
	private String travelOperator;
	
	@Column(nullable=false)
	private String provider;
	
	@Column(nullable=false)
	private String sourceId;
	
	@Column(nullable=false)
	private String sourceName;
	
	@Column(nullable=false)
	private String destinationId;
	
	@Column(nullable=false)
	private String destinationName;
	
	private Date bookingDate;
	
	private String actualFare;
	
	private String bookingRefNo;
	
	private String blockRefNo;
	
	private String apiRefNo;
	
	private String blockId;
	
	private String busTypeName;
	
	private Date journeyDate;
	
	private String departureTime;
	
	private Date returnDate;
	
	private  String serviceTax;
	
	private String serviceCharge;
	
	private String clientId;
	
	@OneToOne(fetch=FetchType.EAGER)
	private TravelSeatDetail seatDetail;
	
	@OneToOne(fetch=FetchType.EAGER)
	private TravelUserDetail userDetail;
	
	
	@OneToOne(fetch=FetchType.EAGER)
	private ClientDetail clientDetail;


	public String getTripId() {
		return tripId;
	}


	public void setTripId(String tripId) {
		this.tripId = tripId;
	}


	public String getTravelOperator() {
		return travelOperator;
	}


	public void setTravelOperator(String travelOperator) {
		this.travelOperator = travelOperator;
	}


	public String getProvider() {
		return provider;
	}


	public void setProvider(String provider) {
		this.provider = provider;
	}


	public String getSourceId() {
		return sourceId;
	}


	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}


	public String getSourceName() {
		return sourceName;
	}


	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}


	public String getDestinationId() {
		return destinationId;
	}


	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}


	public String getDestinationName() {
		return destinationName;
	}


	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}


	public Date getBookingDate() {
		return bookingDate;
	}


	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}


	public String getActualFare() {
		return actualFare;
	}


	public void setActualFare(String actualFare) {
		this.actualFare = actualFare;
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


	public String getApiRefNo() {
		return apiRefNo;
	}


	public void setApiRefNo(String apiRefNo) {
		this.apiRefNo = apiRefNo;
	}


	public String getBlockId() {
		return blockId;
	}


	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}


	public String getBusTypeName() {
		return busTypeName;
	}


	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}


	public Date getJourneyDate() {
		return journeyDate;
	}


	public void setJourneyDate(Date journeyDate) {
		this.journeyDate = journeyDate;
	}


	public String getDepartureTime() {
		return departureTime;
	}


	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}


	public Date getReturnDate() {
		return returnDate;
	}


	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}


	public String getServiceTax() {
		return serviceTax;
	}


	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}


	public String getServiceCharge() {
		return serviceCharge;
	}


	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}


	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public TravelSeatDetail getSeatDetail() {
		return seatDetail;
	}


	public void setSeatDetail(TravelSeatDetail seatDetail) {
		this.seatDetail = seatDetail;
	}


	public TravelUserDetail getUserDetail() {
		return userDetail;
	}


	public void setUserDetail(TravelUserDetail userDetail) {
		this.userDetail = userDetail;
	}


	public ClientDetail getClientDetail() {
		return clientDetail;
	}


	public void setClientDetail(ClientDetail clientDetail) {
		this.clientDetail = clientDetail;
	}
	
	
	
}
