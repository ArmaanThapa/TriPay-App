package com.tripayapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Banks extends AbstractEntity<Long>{

    @Column
    private String name;
    @Column(unique = true)
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
