package com.tripayapp.model;

public enum Salutation {
    MR("Mr."),MRS("Mrs."),MS("Ms.");
    private String value;
    private Salutation(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static Salutation getEnum(String value) {
        if (value == null)
            throw new IllegalArgumentException();
        for (Salutation v : values())
            if (value.equalsIgnoreCase(v.getValue()))
                return v;
        throw new IllegalArgumentException();
    }

}
