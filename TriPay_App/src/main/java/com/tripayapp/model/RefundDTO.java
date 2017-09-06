package com.tripayapp.model;

import com.thirdparty.model.request.JSONWrapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RefundDTO extends SessionDTO implements JSONWrapper{

    private String username;
    private double netAmount;
    private String transactionRefNo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public String getTransactionRefNo() {
        return transactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        this.transactionRefNo = transactionRefNo;
    }


    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try{
            json.put("netAmount",getNetAmount());
            json.put("transactionRefNo",getTransactionRefNo());
            json.put("username",getUsername());
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }
}
