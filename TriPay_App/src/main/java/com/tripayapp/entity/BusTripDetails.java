package com.tripayapp.entity;

import com.tripayapp.model.Status;
import com.tripayapp.model.TripType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class BusTripDetails extends AbstractEntity<Long>{

    @OneToOne(fetch= FetchType.EAGER)
    private BusDetails busDetails;

    @Column
    private String tripId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date journeyDate;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @Column
    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @Column(unique = true)
    private String bookingRefNo;

    @Column(unique = true)
    private String blockingRefNo;

    @Enumerated(EnumType.STRING)
    private Status blockingStatus;

    @Enumerated(EnumType.STRING)
    private Status bookingStatus;

    @OneToOne(fetch = FetchType.EAGER)
    private PQAccountDetail account;

    @Column
    private String seatsId;

    public BusDetails getBusDetails() {
        return busDetails;
    }

    public void setBusDetails(BusDetails busDetails) {
        this.busDetails = busDetails;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getBlockingRefNo() {
        return blockingRefNo;
    }

    public void setBlockingRefNo(String blockingRefNo) {
        this.blockingRefNo = blockingRefNo;
    }

    public Status getBlockingStatus() {
        return blockingStatus;
    }

    public void setBlockingStatus(Status blockingStatus) {
        this.blockingStatus = blockingStatus;
    }

    public Status getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Status bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getSeatsId() {
        return seatsId;
    }

    public void setSeatsId(String seatsId) {
        this.seatsId = seatsId;
    }

    public PQAccountDetail getAccount() {
        return account;
    }

    public void setAccount(PQAccountDetail account) {
        this.account = account;
    }
}
