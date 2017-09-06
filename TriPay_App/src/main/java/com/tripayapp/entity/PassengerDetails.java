package com.tripayapp.entity;

import com.tripayapp.model.Gender;
import com.tripayapp.model.Salutation;

import javax.persistence.*;

@Entity
public class PassengerDetails extends AbstractEntity<Long>{

    @Column
    @Enumerated(EnumType.STRING)
    private Salutation title;

    @Column
    private String name;

    @Column
    private long age;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String mobile;

    @Column
    private String email;

    @Column
    private String alternateMobile;

    @Column
    private String seatNo;

    @Column
    private double fare;

    public Salutation getTitle() {
        return title;
    }

    public void setTitle(Salutation title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }
}
