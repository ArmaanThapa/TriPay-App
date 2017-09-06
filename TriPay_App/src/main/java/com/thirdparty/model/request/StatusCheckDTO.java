package com.thirdparty.model.request;

public class StatusCheckDTO {

    private String merchantRefNo;
    private String secretKey;
    private long merchantId;

    public String getMerchantRefNo() {
        return merchantRefNo;
    }

    public void setMerchantRefNo(String merchantRefNo) {
        this.merchantRefNo = merchantRefNo;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }
}
