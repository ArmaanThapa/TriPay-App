package com.tripayapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class PromoServices extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.EAGER)
    private PromoCode promoCode;

    @OneToOne(fetch = FetchType.EAGER)
    private PQService service;

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    public PQService getService() {
        return service;
    }

    public void setService(PQService service) {
        this.service = service;
    }
}
