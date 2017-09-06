package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.net.URL;

@Entity
public class APIConstant extends AbstractEntity<Long>  {

    @Column(unique = true)
    private String serviceName;
    @Column
    private URL baseUrl;
    @Column
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public URL getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
}
