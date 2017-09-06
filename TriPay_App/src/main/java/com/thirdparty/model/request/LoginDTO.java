package com.thirdparty.model.request;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class LoginDTO implements  JSONWrapper{
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static LoginDTO getInstance(){
        return new LoginDTO();
    }


    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("username",getUsername());
            json.put("password",getPassword());
            return json;
        } catch (JSONException e) {
            return null;
        }

    }
}
