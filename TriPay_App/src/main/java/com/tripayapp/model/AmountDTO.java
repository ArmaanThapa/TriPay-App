package com.tripayapp.model;


import java.util.List;

public class AmountDTO {

    private double walletBalance;
    private double totalLoadMoneyEBS;
    private double totalLoadMoneyVNet;
    private double totalPayable;
    private double totalReceivable;
    private double totalCommission;
    private double merchantPayable;
    private double totalLoadMoneyEBSNow;
    private double totalLoadMoneyVNETNow;
    private double totalPayableNow;
    private double promoBalance;
    private long onlineUsers;
    private long maleUsers;
    private long totalUsers;
    private long femaleUsers;
    private long totalTransaction;
    private List<Double> dailyAmounts;
    private List <Long> dailyCounts;
    private double bankAmount;
    private double mBankAmount;

    public double getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(double bankAmount) {
        this.bankAmount = bankAmount;
    }

    public double getmBankAmount() {
        return mBankAmount;
    }

    public void setmBankAmount(double mBankAmount) {
        this.mBankAmount = mBankAmount;
    }

    public long getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(long onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public long getMaleUsers() {
        return maleUsers;
    }

    public void setMaleUsers(long maleUsers) {
        this.maleUsers = maleUsers;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getFemaleUsers() {
        return femaleUsers;
    }

    public void setFemaleUsers(long femaleUsers) {
        this.femaleUsers = femaleUsers;
    }

    public long getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(long totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public List<Double> getDailyAmounts() {
        return dailyAmounts;
    }

    public void setDailyAmounts(List<Double> dailyAmounts) {
        this.dailyAmounts = dailyAmounts;
    }

    public List<Long> getDailyCounts() {
        return dailyCounts;
    }

    public void setDailyCounts(List<Long> dailyCounts) {
        this.dailyCounts = dailyCounts;
    }

    public double getPromoBalance() {
        return promoBalance;
    }

    public void setPromoBalance(double promoBalance) {
        this.promoBalance = promoBalance;
    }

    public double getTotalLoadMoneyEBSNow() {
        return totalLoadMoneyEBSNow;
    }

    public void setTotalLoadMoneyEBSNow(double totalLoadMoneyEBSNow) {
        this.totalLoadMoneyEBSNow = totalLoadMoneyEBSNow;
    }

    public double getTotalLoadMoneyVNETNow() {
        return totalLoadMoneyVNETNow;
    }

    public void setTotalLoadMoneyVNETNow(double totalLoadMoneyVNETNow) {
        this.totalLoadMoneyVNETNow = totalLoadMoneyVNETNow;
    }

    public double getTotalPayableNow() {
        return totalPayableNow;
    }

    public void setTotalPayableNow(double totalPayableNow) {
        this.totalPayableNow = totalPayableNow;
    }

    public double getMerchantPayable() {
        return merchantPayable;
    }

    public void setMerchantPayable(double merchantPayable) {
        this.merchantPayable = merchantPayable;
    }

    public double getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(double totalCommission) {
        this.totalCommission = totalCommission;
    }

    public double getTotalReceivable() {
        return totalReceivable;
    }

    public void setTotalReceivable(double totalReceivable) {
        this.totalReceivable = totalReceivable;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getTotalLoadMoneyEBS() {
        return totalLoadMoneyEBS;
    }

    public void setTotalLoadMoneyEBS(double totalLoadMoneyEBS) {
        this.totalLoadMoneyEBS = totalLoadMoneyEBS;
    }

    public double getTotalLoadMoneyVNet() {
        return totalLoadMoneyVNet;
    }

    public void setTotalLoadMoneyVNet(double totalLoadMoneyVNet) {
        this.totalLoadMoneyVNet = totalLoadMoneyVNet;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }
}
