package com.tripayapp.entity;

import com.tripayapp.model.Status;

import javax.persistence.*;

@Entity
public class TPTransaction extends AbstractEntity<Long>{

    @Column
    private String orderId;

    @Column
    private double amount;

    @Column
    private String transactionRefNo;

    @OneToOne
    private User merchant;

    @OneToOne
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionRefNo() {
        return transactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        this.transactionRefNo = transactionRefNo;
    }

    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
