package com.tripayapp.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class RedeemCode extends AbstractEntity<Long>{

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.EAGER)
	private PromoCode promoCode;
	
	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	public PromoCode getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(PromoCode promoCode) {
		this.promoCode = promoCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
