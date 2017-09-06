package com.tripayapp.model;

import com.thirdparty.model.request.JSONWrapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class SharePointDTO implements JSONWrapper {

    private String sessionId;
    private int points;
    private String senderUsername;
    private String receiverUsername;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try{
          json.put("points",getPoints());
            json.put("senderUsername",getSenderUsername());
            json.put("receiverUsername",getReceiverUsername());
        }catch(JSONException e){

        }
        return json;
    }
}
