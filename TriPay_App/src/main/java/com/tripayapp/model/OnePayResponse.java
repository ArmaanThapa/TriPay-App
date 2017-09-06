package com.tripayapp.model;

import com.tripayapp.model.mobile.ResponseDTO;

public class OnePayResponse extends ResponseDTO{

    private String serviceCode;
    private String serviceName;
    private Status tStatus;
    private String json;

    public Status gettStatus() {
        return tStatus;
    }

    public void settStatus(Status tStatus) {
        this.tStatus = tStatus;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
