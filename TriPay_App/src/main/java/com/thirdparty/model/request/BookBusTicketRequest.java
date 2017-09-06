package com.thirdparty.model.request;

import java.util.Date;

import com.tripayapp.model.UserType;

public class BookBusTicketRequest {

	private String sessionId;
	
//	private UserType userType;
	
	private String tripId;
	
	private String boardingId;
	
	private String boardingAddress;
	
	private String ipAddress;
	
	private String tokenKey;
	
	private String travelOperator;
	
	private String provider;
	
	private String sourceId;
	
	private String sourceName;
	
	private String destinationId;
	
	private String destinationName;
	
	private Date bookingDate;
	
	private String bookingStatus;
	
	private String actualFare;
	
	private String blockingRefNo;
	
	private String bookingRefNo;
	
	private String busTypeName;
	
	private String apiRefNo;
	
	private Date journeyDate;
	
	private String departureTime;
	
	private Date returnDate;
	
	private String referenceNo;
	
	private String seatNo;
	
	private String ladiesSeat;
	
	private String fare;
	
	private String netFare;
	
    private String noOfSeats;
	
    private String salutation;
    
	private String name;
	
	private String age;
	
	private String gender;
	
	private String mobileNo;
	
	private String emergencyMobileNo;
	
	private String emailId;
	
	private String postalCode;
	
	private String city;
	
	private String state;
	
	private String address;
	
	private String partialCancellationAllowed;
	
	private String serviceTax;
	
	private String serviceCharge;
	
	private String cancelPolicy;
	
	private String transactionRefNo;
	
	private String blockId;
	
	private String clientId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	/*public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
*/
	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getBoardingId() {
		return boardingId;
	}

	public void setBoardingId(String boardingId) {
		this.boardingId = boardingId;
	}

	public String getBoardingAddress() {
		return boardingAddress;
	}

	public void setBoardingAddress(String boardingAddress) {
		this.boardingAddress = boardingAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
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

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
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

	public String getBusTypeName() {
		return busTypeName;
	}

	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
	}

	public String getApiRefNo() {
		return apiRefNo;
	}

	public void setApiRefNo(String apiRefNo) {
		this.apiRefNo = apiRefNo;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public Date getJourneyDate() {
		return journeyDate;
	}

	public void setJourneyDate(Date journeyDate) {
		this.journeyDate = journeyDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public String getLadiesSeat() {
		return ladiesSeat;
	}

	public void setLadiesSeat(String ladiesSeat) {
		this.ladiesSeat = ladiesSeat;
	}

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
	}

	public String getNetFare() {
		return netFare;
	}

	public void setNetFare(String netFare) {
		this.netFare = netFare;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmergencyMobileNo() {
		return emergencyMobileNo;
	}

	public void setEmergencyMobileNo(String emergencyMobileNo) {
		this.emergencyMobileNo = emergencyMobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPartialCancellationAllowed() {
		return partialCancellationAllowed;
	}

	public void setPartialCancellationAllowed(String partialCancellationAllowed) {
		this.partialCancellationAllowed = partialCancellationAllowed;
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

	public String getCancelPolicy() {
		return cancelPolicy;
	}

	public void setCancelPolicy(String cancelPolicy) {
		this.cancelPolicy = cancelPolicy;
	}

	public String getBlockingRefNo() {
		return blockingRefNo;
	}

	public void setBlockingRefNo(String blockingRefNo) {
		this.blockingRefNo = blockingRefNo;
	}

	public String getTransactionRefNo() {
		return transactionRefNo;
	}

	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(String noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

}
