package com.tripayapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class BusPassengerTrip extends AbstractEntity<Long>{

    @OneToOne
    private BusTripDetails busTripDetails;

    @OneToOne
    private PassengerDetails passengerDetails;


    public BusTripDetails getBusTripDetails() {
        return busTripDetails;
    }

    public void setBusTripDetails(BusTripDetails busTripDetails) {
        this.busTripDetails = busTripDetails;
    }

    public PassengerDetails getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(PassengerDetails passengerDetails) {
        this.passengerDetails = passengerDetails;
    }
}
