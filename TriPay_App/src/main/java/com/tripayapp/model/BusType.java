package com.tripayapp.model;

public enum BusType {

    AC_SLEEPER("AC Sleeper"),NON_AC_SLEEPER("Non AC Semi Sleeper"),AC_SEMI_SLEEPER("AC Semi Sleeper"),NON_AC_SEMI_SLEEPER("Non AC Semi Sleeper");

    private String value;

    private BusType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static BusType getEnum(String value) {
        if (value == null)
            throw new IllegalArgumentException();
        for (BusType v : values())
            if (value.equalsIgnoreCase(v.getValue()))
                return v;
        throw new IllegalArgumentException();
    }


}
