package com.tripayapp.entity;

import org.hibernate.annotations.LazyCollection;

import javax.persistence.*;
import java.util.List;

@Entity
public class PGDetails extends AbstractEntity<Long>{


    @Column(nullable=false)
    private String ipAddress;

    @Column(nullable=false)
    private String token;

    @OneToOne
    private PQService service;

    @OneToOne
    private User user;

    private String successURL;

    private String returnURL;

    private boolean store;

    private boolean paymentGateway;

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public boolean isPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(boolean paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getSuccessURL() {
        return successURL;
    }

    public void setSuccessURL(String successURL) {
        this.successURL = successURL;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public PQService getService() {
        return service;
    }

    public void setService(PQService service) {
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
