package com.tripayapp.model;

public class FavouriteRequest extends SessionDTO{
    private String transactionRefNo;
    private long id;

    private boolean favourite;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getTransactionRefNo() {
        return transactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        this.transactionRefNo = transactionRefNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FavouriteRequest{" +
                "transactionRefNo='" + transactionRefNo + '\'' +
                ", id=" + id +
                '}';
    }
}
