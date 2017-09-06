package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LocationDetails extends AbstractEntity<Long> {

    @Column(nullable = false)
    private String locality;

    @Column(nullable = false,unique = true)
    private String pinCode;

    @Column(nullable = false)
    private String regionName;

    @Column(nullable = false)
    private String circleName;

    @Column(nullable = false)
    private String districtName;

    @Column(nullable = false)
    private String stateName;


    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
