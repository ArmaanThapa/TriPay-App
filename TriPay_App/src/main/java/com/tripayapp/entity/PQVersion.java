package com.tripayapp.entity;

import com.tripayapp.model.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class PQVersion extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private int versionCode;

    @Column
    private int subversionCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getSubversionCode() {
        return subversionCode;
    }

    public void setSubversionCode(int subversionCode) {
        this.subversionCode = subversionCode;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
