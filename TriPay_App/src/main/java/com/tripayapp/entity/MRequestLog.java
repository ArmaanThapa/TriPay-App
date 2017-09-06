package com.tripayapp.entity;



import com.tripayapp.model.Status;
import javax.persistence.*;

@Entity
public class MRequestLog extends AbstractEntity<Long>{
    @Lob
    private String request;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private PGDetails pgDetails;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PGDetails getPgDetails() {
        return pgDetails;
    }

    public void setPgDetails(PGDetails pgDetails) {
        this.pgDetails= pgDetails;
    }
}
