package com.tripayapp.entity;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class InviteLog extends AbstractEntity<Long>{

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    private String contactNo;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
