package com.tripayapp.model.travel;

import com.tripayapp.model.BusType;

public class BusDetailDTO {

    private String source;

    private String destination;

    private String boardingId;

    private String boardingAddress;

    private String busOperator;

    private BusType busType;

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

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }
}
