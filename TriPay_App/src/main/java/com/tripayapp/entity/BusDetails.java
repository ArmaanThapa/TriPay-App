package com.tripayapp.entity;

import com.tripayapp.model.BusType;
import com.tripayapp.model.TripType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class BusDetails extends AbstractEntity<Long>{

	private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column
    private String boardingId;

    @Column
    private String boardingAddress;

    @Column
    private String busOperator;

    @Column
    @Enumerated(EnumType.STRING)
    private BusType busType;

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

    public String getBusOperator() {
        return busOperator;
    }

    public void setBusOperator(String busOperator) {
        this.busOperator = busOperator;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public BusType getBusType() {
		return busType;
	}

	public void setBusType(BusType busType) {
		this.busType = busType;
	}
}
