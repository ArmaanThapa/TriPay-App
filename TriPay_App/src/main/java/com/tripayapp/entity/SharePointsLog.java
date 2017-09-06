package com.tripayapp.entity;

import com.tripayapp.model.Status;

import javax.persistence.*;

@Entity
public class SharePointsLog extends AbstractEntity<Long>{

    @ManyToOne(optional = false)
    private PQAccountDetail account;

    private long points;

    private String transactionRefNo;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    private String request;

    public PQAccountDetail getAccount() {
        return account;
    }

    public String getTransactionRefNo() {
        return transactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        this.transactionRefNo = transactionRefNo;
    }

    public void setAccount(PQAccountDetail account) {
        this.account = account;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
