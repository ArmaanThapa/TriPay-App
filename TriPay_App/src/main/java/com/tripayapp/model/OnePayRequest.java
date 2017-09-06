package com.tripayapp.model;


public class OnePayRequest extends SessionDTO{
    private String transactionRefNo;

    public String getTransactionRefNo() {
        return transactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        this.transactionRefNo = transactionRefNo;
    }
}
