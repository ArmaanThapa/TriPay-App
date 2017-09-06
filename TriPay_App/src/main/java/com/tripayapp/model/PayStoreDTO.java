package com.tripayapp.model;

import com.thirdparty.model.request.JSONWrapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class PayStoreDTO extends SessionDTO implements JSONWrapper{

    private long id;
    private double netAmount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("id", getId());
            json.put("netAmount", getNetAmount());
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }
}
