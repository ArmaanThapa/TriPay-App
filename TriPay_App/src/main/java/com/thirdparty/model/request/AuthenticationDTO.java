package com.thirdparty.model.request;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AuthenticationDTO extends AbstractDTO implements JSONWrapper{
    private String ipAddress;
    private String amount;
    private String hash;
    private String transactionID;
    private String additionalInfo;

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("amount", getAmount());
            jsonObject.put("token", getToken());
            jsonObject.put("hash", getHash());
            jsonObject.put("ipAddress",getIpAddress());
            return jsonObject;
        }catch(JSONException ex){
            return null;
        }

    }
}
