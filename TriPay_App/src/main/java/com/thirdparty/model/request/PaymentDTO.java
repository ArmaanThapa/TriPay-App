package com.thirdparty.model.request;

public class PaymentDTO extends AbstractDTO{
    private String sessionId;
    private String transactionID;
    private double walletAmount;
    private double netAmount;
    private double amountToLoad;
    private boolean useWallet;
    private String serviceCode;

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getAmountToLoad() {
        return amountToLoad;
    }

    public void setAmountToLoad(double amountToLoad) {
        this.amountToLoad = amountToLoad;
    }

    public boolean isUseWallet() {
        return useWallet;
    }

    public void setUseWallet(boolean useWallet) {
        this.useWallet = useWallet;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
