package com.tripayapp.model;

public enum TripType {
    ONE_WAY("One Way"),ROUND_TRIP("Round Trip");
    String trip;
    private TripType(String trip){
        this.trip = trip;
    }

    @Override
    public String toString() {
        return trip;
    }

    public String getValue() {
        return trip;
    }

    public static TripType getEnum(String value) {
        if (value == null)
            throw new IllegalArgumentException();
        for (TripType v : values())
            if (value.equalsIgnoreCase(v.getValue()))
                return v;
        throw new IllegalArgumentException();
    }
}
