package com.tripayapp.entity;


import com.tripayapp.model.Status;

import javax.persistence.*;

@Entity
public class VBankAccountDetail extends AbstractEntity<Long>{

    @Column(unique=true,nullable = false)
    private String accountNumber;

    @Column(nullable = true)
    private String accountName;

    @Column(nullable = false)
    private String mobileNumber;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String otp;


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
